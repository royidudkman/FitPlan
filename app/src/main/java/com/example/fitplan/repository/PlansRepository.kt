package com.example.fitplan.repository

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface PlansRepository {
    suspend fun addPlan(title: String , description : String, image: Bitmap?, exercises: List<Exercise>) : Resource<Void>
    suspend fun addSocialPlan(title: String, description: String, image: Bitmap?, exercises: List<Exercise>): Resource<Void>
    suspend fun deletePlan(planId: String): Resource<Void>
    suspend fun deleteSocialPlan(planId: String): Resource<Void>
    suspend fun getPlan(planId: String) : Resource<Plan>
    suspend fun getPlans():Resource<List<Plan>>

    fun getPlansFlow(): Flow<Resource<List<Plan>>>
    suspend fun getPlansLiveData(data: MutableLiveData<Resource<List<Plan>>>)
    suspend fun getSocialPlansLiveData(data: MutableLiveData<Resource<List<Plan>>>)
    //fun getPlansLiveData(data : MutableLiveData<Resource<List<Plan>>>)
}