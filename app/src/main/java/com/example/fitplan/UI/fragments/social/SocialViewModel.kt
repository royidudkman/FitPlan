package com.example.fitplan.UI.fragments.social

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan
import com.example.fitplan.repository.AuthRepository
import com.example.fitplan.repository.PlansRepository
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.launch

class SocialViewModel (private val authRep: AuthRepository, val planRep : PlansRepository) : ViewModel() {


    val _plansStatus : MutableLiveData<Resource<List<Plan>>> = MutableLiveData()
    val planStatus : LiveData<Resource<List<Plan>>> = _plansStatus

    private val _addPlanStatus = MutableLiveData<Resource<Void>>()
    val addPlanStatus: LiveData<Resource<Void>> = _addPlanStatus

    private val _deletePlanStatus = MutableLiveData<Resource<Void>>()
    val deletePlanStatus: LiveData<Resource<Void>> = _deletePlanStatus

    val planShare = MutableLiveData<Plan>()
    init{
        viewModelScope.launch {
            planRep.getSocialPlansLiveData(_plansStatus)
        }
    }

    fun addSocialPlan(planId : String,title: String, description : String, image: Bitmap?, exercises: List<Exercise>){
        viewModelScope.launch {
            if(title.isEmpty())
                _addPlanStatus.postValue(Resource.Error("Empty plan title"))
            else {
                _addPlanStatus.postValue(Resource.Loading())
                _addPlanStatus.postValue(planRep.addSocialPlan(planId, title,description,image,exercises))
            }
        }
    }

    fun deleteSocialPlan(id : String){
        viewModelScope.launch {
            if(id.isEmpty())
                _deletePlanStatus.postValue(Resource.Error("Empty plan id"))
            else {
                _deletePlanStatus.postValue(Resource.Loading())
                _deletePlanStatus.postValue(planRep.deleteSocialPlan(id))
            }
        }
    }



    class SocialViewModelFactory(val authRepo: AuthRepository, val planRep: PlansRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SocialViewModel(authRepo, planRep) as T
        }
    }
}