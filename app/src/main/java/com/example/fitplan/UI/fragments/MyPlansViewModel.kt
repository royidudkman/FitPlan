package com.example.fitplan.UI.fragments

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitplan.SharedViewModel
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan
import com.example.fitplan.repository.AuthRepository
import com.example.fitplan.repository.PlansRepository
import com.google.android.gms.tasks.Task
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.launch

class MyPlansViewModel(private val authRep:AuthRepository,val planRep : PlansRepository) : ViewModel() {


    val _plansStatus : MutableLiveData<Resource<List<Plan>>> = MutableLiveData()
    val planStatus : LiveData<Resource<List<Plan>>> = _plansStatus

    private val _addPlanStatus = MutableLiveData<Resource<Void>>()
    val addPlanStatus: LiveData<Resource<Void>> = _addPlanStatus

    private val _deletePlanStatus = MutableLiveData<Resource<Void>>()
    val deletePlanStatus: LiveData<Resource<Void>> = _deletePlanStatus




    init{
        viewModelScope.launch {
            planRep.getPlansLiveData(_plansStatus)
        }
    }

    fun fetchPlans() {
        viewModelScope.launch {
            planRep.getPlansLiveData(_plansStatus)
        }
    }

    fun addPlan(title: String, description : String, image: Bitmap?, exercises: List<Exercise>){
        viewModelScope.launch {
            if(title.isEmpty())
                _addPlanStatus.postValue(Resource.Error("Empty plan title"))
            else {
                _addPlanStatus.postValue(Resource.Loading())
                _addPlanStatus.postValue(planRep.addPlan(title,description,image,exercises))
            }
        }
    }

    fun deletePlan(id : String){
        viewModelScope.launch {
            if(id.isEmpty())
                _deletePlanStatus.postValue(Resource.Error("Empty plan id"))
            else {
                _deletePlanStatus.postValue(Resource.Loading())
                _deletePlanStatus.postValue(planRep.deletePlan(id))
            }
        }
    }

    fun deleteExerciseFromPlan(planId: String, exerciseId: String) {
        viewModelScope.launch {
            if (planId.isEmpty() || exerciseId.isEmpty()) {
                _deletePlanStatus.postValue(Resource.Error("Empty plan or exercise id"))
            } else {
                _deletePlanStatus.postValue(Resource.Loading())
                val result = planRep.deleteExerciseFromPlan(planId, exerciseId)
                _deletePlanStatus.postValue(result)
            }
        }
    }


    class MyPlansViewModelFactory(val authRepo: AuthRepository, val planRep: PlansRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyPlansViewModel(authRepo, planRep) as T
        }
    }
}