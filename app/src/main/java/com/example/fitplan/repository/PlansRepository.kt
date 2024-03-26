package com.example.fitplan.repository

import androidx.lifecycle.MutableLiveData
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface PlansRepository {
    suspend fun addPlan(title: String , description : String, image: Int, exercises: List<Exercise>) : Resource<Void>
    suspend fun deletePlan(planId: String): Resource<Void>
    suspend fun getPlan(planId: String) : Resource<Plan>
    suspend fun getPlans():Resource<List<Plan>>

    fun getPlansFlow(): Flow<Resource<List<Plan>>>
    fun getPlansLiveData(data : MutableLiveData<Resource<List<Plan>>>)
}