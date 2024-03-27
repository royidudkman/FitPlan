package com.example.fitplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitplan.model.Plan

class SharedViewModel : ViewModel() {
    private val _selectedPlan = MutableLiveData<Plan>()
    val selectedPlan: LiveData<Plan> get() = _selectedPlan

    fun setSelectedPlan(plan: Plan) {
        _selectedPlan.value = plan
    }
}