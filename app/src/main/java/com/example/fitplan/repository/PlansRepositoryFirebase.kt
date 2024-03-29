package com.example.fitplan.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import safeCall
import java.io.ByteArrayOutputStream

class PlansRepositoryFirebase : PlansRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userPlansCollection = currentUser?.let{firestore.collection("users").document(it.uid).collection("exercises")}
    private val socialPlansCollection = firestore.collection("SocialPlans")
    override suspend fun addPlan(title: String, description: String, image: Bitmap?, exercises: List<Exercise>): Resource<Void> = withContext(Dispatchers.IO) {
        currentUser?.let { user ->
            userPlansCollection?.let { plansCollection ->
                safeCall {
                    var base64Bitmap: String? = image?.let { bitmap ->
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                        Base64.encodeToString(byteArray, Base64.DEFAULT)
                    }
                    if (base64Bitmap == null) base64Bitmap = ""

                    val planId = plansCollection.document().id
                    val plan = Plan(planId, title, description, base64Bitmap, null, emptyList())

                    // Save the plan document under user's collection
                    val addition = plansCollection.document(planId).set(plan).await()

                    // Save exercises as subcollection under the plan
                    val exercisesCollectionRef = plansCollection.document(planId).collection("exercises")
                    exercises.forEachIndexed { index, exercise ->
                        exercisesCollectionRef.document("exercise_${index + 1}").set(exercise).await()
                    }

                    Resource.Success(addition)
                }
            } ?: Resource.Error("User plans collection is null")
        } ?: Resource.Error("Current user is null")
    }


    override suspend fun addSocialPlan(planId: String, title: String, description: String, image: Bitmap?, exercises: List<Exercise>): Resource<Void> = withContext(Dispatchers.IO) {
        safeCall {
            var base64Bitmap: String? = image?.let { bitmap ->
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
            if (base64Bitmap == null) base64Bitmap = ""

            val plan = Plan(planId, title, description, base64Bitmap, null, exercises)

            // Save the plan document under SocialPlans collection
            val addition = socialPlansCollection.document(planId).set(plan).await()
            Resource.Success(addition)
        }
    }

    override suspend fun deletePlan(planId: String): Resource<Void> = withContext(Dispatchers.IO) {
        currentUser?.let { user ->
            userPlansCollection?.let { plansCollection ->
                safeCall {
                    // Delete the plan document from user's collection
                    val result = plansCollection.document(planId).delete().await()
                    Resource.Success(result)
                }
            } ?: Resource.Error("User plans collection is null")
        } ?: Resource.Error("Current user is null")
    }

    override suspend fun deleteExerciseFromPlan(planId: String, exerciseId: String): Resource<Void> = withContext(Dispatchers.IO) {
        currentUser?.let { user ->
            userPlansCollection?.let { plansCollection ->
                safeCall {
                    val planRef = plansCollection.document(planId)
                    val exercisesCollectionRef = planRef.collection("exercises")

                    // Delete the exercise document from the exercises subcollection
                    val delete = exercisesCollectionRef.document(exerciseId).delete().await()

                    Resource.Success(delete)
                }
            } ?: Resource.Error("User plans collection is null")
        } ?: Resource.Error("Current user is null")
    }



    override suspend fun deleteSocialPlan(planId: String): Resource<Void> = withContext(Dispatchers.IO) {
        safeCall {
            // Delete the plan document from the SocialPlans collection
            val result = socialPlansCollection.document(planId).delete().await()
            Resource.Success(result)
        }
    }

    override suspend fun getPlan(planId: String): Resource<Plan> = withContext(Dispatchers.IO) {
        currentUser?.let { user ->
            userPlansCollection?.let { plansCollection ->
                safeCall {
                    val planDocument = plansCollection.document(planId).get().await()
                    val plan = planDocument.toObject(Plan::class.java)
                    plan?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Plan not found")
                }
            } ?: Resource.Error("User plans collection is null")
        } ?: Resource.Error("Current user is null")
    }

    override suspend fun getPlans(): Resource<List<Plan>> = withContext(Dispatchers.IO) {
        currentUser?.let { user ->
            userPlansCollection?.let { plansCollection ->
                safeCall {
                    val plansSnapshot = plansCollection.get().await()
                    val plans = plansSnapshot.toObjects(Plan::class.java)
                    Resource.Success(plans)
                }
            } ?: Resource.Error("User plans collection is null")
        } ?: Resource.Error("Current user is null")
    }

    override fun getPlansFlow(): Flow<Resource<List<Plan>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlansLiveData(data: MutableLiveData<Resource<List<Plan>>>) = withContext(Dispatchers.IO) {
        data.postValue(Resource.Loading())

        try {
            val snapshot = userPlansCollection?.orderBy("title")?.get()?.await()
            val plans = mutableListOf<Plan>()

            snapshot?.documents?.forEach { document ->
                val planId = document.id
                val planTitle = document.getString("title") ?: ""
                val planDescription = document.getString("description") ?: ""
                var base64Bitmap: String? = document.getString("imageString") ?: ""
                val bitmap: Bitmap? = null // Assuming the bitmap is not stored directly in the document

                if(base64Bitmap == null)
                    base64Bitmap = ""

                val exercises = mutableListOf<Exercise>()
                val exercisesSnapshot = document.reference.collection("exercises").get().await()

                exercisesSnapshot.documents.forEach { exerciseDocument ->
                    val name = exerciseDocument.getString("name") ?: ""
                    val description = exerciseDocument.getString("description") ?: ""
                    val bodyPart = exerciseDocument.getString("bodyPart") ?: ""
                    val image = exerciseDocument.getLong("image")?.toInt() ?: 0
                    val reps = exerciseDocument.getLong("reps")?.toInt() ?: 0
                    val time = exerciseDocument.getLong("time") ?: 0
                    val id = exerciseDocument.id // Get the document ID as exercise ID

                    val exercise = Exercise(name, description, bodyPart, image, reps, time)
                    exercise.id = id
                    exercises.add(exercise)
                }

                val plan = Plan(planId, planTitle, planDescription, base64Bitmap, bitmap, exercises)
                plans.add(plan)
            }

            if (plans.isNotEmpty()) {
                data.postValue(Resource.Success(plans))
            } else {
                data.postValue(Resource.Error("No Data"))
            }
        } catch (e: Exception) {
            data.postValue(Resource.Error(e.localizedMessage))
        }
    }


    override suspend fun getSocialPlansLiveData(data: MutableLiveData<Resource<List<Plan>>>) = withContext(Dispatchers.IO) {
        data.postValue(Resource.Loading())

        try {
            val snapshot = socialPlansCollection.orderBy("title").get().await()
            val plans = mutableListOf<Plan>()

            snapshot?.documents?.forEach { document ->
                val planId = document.id
                val planTitle = document.getString("title") ?: ""
                val planDescription = document.getString("description") ?: ""
                var base64Bitmap: String? = document.getString("imageString") ?: ""
                val bitmap: Bitmap? = null // Assuming the bitmap is not stored directly in the document

                if(base64Bitmap == null)
                    base64Bitmap = ""

                val exercises = mutableListOf<Exercise>()

                // Fetch exercises from the "exercises" field array
                val exercisesData = document.get("exercises") as? List<Map<String, Any>> // Adjust the type if needed

                exercisesData?.forEach { exerciseData ->
                    val name = exerciseData["name"] as? String ?: ""
                    val description = exerciseData["description"] as? String ?: ""
                    val bodyPart = exerciseData["bodyPart"] as? String ?: ""
                    val image = (exerciseData["image"] as? Long)?.toInt() ?: 0
                    val reps = (exerciseData["reps"] as? Long)?.toInt() ?: 0
                    val time = exerciseData["time"] as? Long ?: 0
                    val id = exerciseData["id"] as? String ?: ""

                    val exercise = Exercise(name, description, bodyPart, image, reps, time)
                    exercise.id = id
                    exercises.add(exercise)
                }

                val plan = Plan(planId, planTitle, planDescription, base64Bitmap, bitmap, exercises)
                plans.add(plan)
            }

            if (plans.isNotEmpty()) {
                data.postValue(Resource.Success(plans))
            } else {
                data.postValue(Resource.Error("No Data"))
            }
        } catch (e: Exception) {
            data.postValue(Resource.Error(e.localizedMessage))
        }
    }



}