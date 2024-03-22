package com.example.fitplan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitplan.model.Exercise
import com.example.fitplan.repository.ExerciseRepository
import kotlinx.coroutines.launch

class ExercisesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExerciseRepository(application)
    val exercises : LiveData<List<Exercise>>? = repository.getExercises()

    private val _chosenExercise = MutableLiveData<Exercise>()
    val chosenExercise : LiveData<Exercise> get() = _chosenExercise

    fun setItem(exercise: Exercise){
        _chosenExercise.value = exercise
    }

    fun addItem(exercise: Exercise){
        viewModelScope.launch{
            repository.addExercise(exercise)
        }
    }
    fun deleteItem(exercise: Exercise){
        viewModelScope.launch{
            repository.deleteExercise(exercise)
        }

    }
    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}