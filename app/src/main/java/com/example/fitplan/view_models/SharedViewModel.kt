package com.example.fitplan.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan

class SharedViewModel : ViewModel() {
    private val _selectedPlan = MutableLiveData<Plan>()
    val selectedPlan: LiveData<Plan> get() = _selectedPlan

    private val _selectedExercise = MutableLiveData<Exercise>()
    val selectedExercise: LiveData<Exercise> get() = _selectedExercise

    private val _exerciseToPlan = MutableLiveData<List<Exercise>>()
    val exerciseToPlan: LiveData<List<Exercise>> get() = _exerciseToPlan

    private val _sharedPlans: MutableLiveData<List<Plan>> = MutableLiveData()
    val sharedPlans: LiveData<List<Plan>> get() = _sharedPlans

    var totalMinutes : Int = 0
    var totalSeconds : Int = 0
    var totalReps : Int = 0

    fun setSelectedPlan(plan: Plan) {
        _selectedPlan.value = plan
    }
    fun addExerciseToPlan(exercise: Exercise) {
        val currentList = _exerciseToPlan.value?.toMutableList() ?: mutableListOf()
        currentList.add(exercise)
        _exerciseToPlan.value = currentList
    }

    fun cleanExerciseToPlan(){
        _exerciseToPlan.value = emptyList()
    }
    fun setSelectedExercise(exercise: Exercise) {
        _selectedExercise.value = exercise
    }

    fun setSharedPlans(plans:List<Plan>){
        _sharedPlans.value = plans
    }


}