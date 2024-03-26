package com.example.fitplan.repository

import androidx.lifecycle.MutableLiveData
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import safeCall

class PlansRepositoryFirebase : PlansRepository {

    private val planRef = FirebaseFirestore.getInstance().collection("users")
    override suspend fun addPlan(title: String, description: String, image: Int, exercises: List<Exercise>): Resource<Void> = withContext(Dispatchers.IO){

        safeCall {
            val planId = planRef.document().id
            val plan = Plan(planId, title ,description, image, exercises)
            val addition = planRef.document(planId).set(plan).await()

            val exercisesCollectionRef = planRef.document(planId).collection("exercises")
            exercises.forEachIndexed { index, exercise ->
                exercisesCollectionRef.document("exercise_$index").set(exercise).await()
            }

            Resource.Success(addition)

        }
    }

    override suspend fun deletePlan(planId: String): Resource<Void> = withContext(Dispatchers.IO){
        safeCall {
            val result = planRef.document(planId).delete().await()
            Resource.Success(result)
        }
    }

    override suspend fun getPlan(planId: String): Resource<Plan> = withContext(Dispatchers.IO){
        safeCall {
            val result = planRef.document(planId).get().await()
            val plan = result.toObject(Plan::class.java)
            Resource.Success(plan!!)
        }
    }

    override suspend fun getPlans(): Resource<List<Plan>> = withContext(Dispatchers.IO){
        safeCall {
            val result = planRef.get().await()
            val plans = result.toObjects(Plan::class.java)
            Resource.Success(plans)
        }
    }

    override fun getPlansFlow(): Flow<Resource<List<Plan>>> {
        TODO("Not yet implemented")
    }

//    override fun getPlansLiveData(data: MutableLiveData<Resource<List<Plan>>>) {
//        data.postValue(Resource.Loading())
//        planRef.orderBy("title").addSnapshotListener{ snapshot, e ->
//            if(e !=null){
//                data.postValue(Resource.Error(e.localizedMessage))
//            }
//            if(snapshot != null && !snapshot.isEmpty){
//                data.postValue(Resource.Success(snapshot.toObjects(Plan::class.java)))
//            }
//            else{
//                data.postValue(Resource.Error("No Data"))
//            }
//        }
//    }
}