package il.co.syntax.firebasemvvm.repository.FirebaseImpl

import com.example.fitplan.model.Exercise
import com.example.fitplan.model.User
import com.example.fitplan.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import safeCall

class AuthRepositoryFirebase : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore.getInstance().collection("users")

     override suspend fun currentUser() : Resource<User>{
        return withContext(Dispatchers.IO){
            safeCall {
                val user = userRef.document(firebaseAuth.currentUser!!.uid).get().await().toObject(
                    User::class.java)!!
                Resource.Success(user)
            }
        }
    }
    override suspend fun login(email:String, password:String) : Resource<User>{
        return withContext(Dispatchers.IO) {
            safeCall {
                val result  = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val user = userRef.document(result.user?.uid!!).get().await().toObject(User::class.java)!!
                Resource.Success(user)
            }
        }
    }
    override suspend fun createUser(userEmail:String,userName:String, userPassword: String) : Resource<User>{
        return withContext(Dispatchers.IO){
            safeCall {
                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).await()
                val userId = registrationResult.user?.uid!!
                val newUser = User(userEmail,userName)
                userRef.document(userId).set(newUser).await()
                Resource.Success(newUser)
            }
        }
    }

    override suspend fun getUsername(userId: String): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = userRef.document(userId).get().await()
                if (documentSnapshot.exists()) {
                    val userName = documentSnapshot.getString("userName") ?: ""
                    Resource.Success(userName)
                } else {
                    Resource.Error("User document does not exist")
                }
            } catch (e: Exception) {
                Resource.Error("Error retrieving username: ${e.message}")
            }
        }
    }

    override suspend fun setUsername(userId: String, newUserName: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            safeCall {
                userRef.document(userId).update("userName", newUserName).await()
                Resource.Success(Unit)
                Resource.Error("Error setting username")
            }
        }
    }



    override fun logout(){
       firebaseAuth.signOut()
    }
}