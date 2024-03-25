package com.example.fitplan.repository

import com.example.fitplan.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import safeCall


interface AuthRepository {
    suspend fun currentUser() : Resource<User>
    suspend fun login(email:String, password:String) : Resource<User>

    suspend fun createUser(userEmail:String, userPassword:String) : Resource<User>
    fun logout()

}