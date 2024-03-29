package com.example.fitplan

import android.app.ActivityManager
import android.app.Application
import android.content.ContentProvider
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.fitplan.model.Exercise
import com.example.fitplan.repository.ExerciseRepository
import kotlinx.coroutines.launch
import java.util.Locale

class ExercisesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExerciseRepository(application)
    val exercises : LiveData<List<Exercise>>? = repository.getExercises()

    private val _filteredExercises = MutableLiveData<List<Exercise>>()
    val filteredExercises : LiveData<List<Exercise>> get() = _filteredExercises


    private val _chosenExercise = MutableLiveData<Exercise>()
    val chosenExercise : LiveData<Exercise> get() = _chosenExercise

    private val _selectedBodyPart = MutableLiveData("Back")
    val selectedBodyPart: LiveData<String> get() = _selectedBodyPart
    fun setSelectedBodyPart(bodyPart: String) {
        _selectedBodyPart.value = bodyPart
    }
//    init {
//        // Observe LiveData in ViewModel
//        exercises?.observeForever {
//
//        }
//    }
    interface TimerCallback {
        fun startTimer(milliseconds: Long)
    }

    var timerCallback : TimerCallback? = null



    fun setExercise(exercise: Exercise){
        _chosenExercise.value = exercise
    }

    fun addExercise(exercise: Exercise){
        viewModelScope.launch{
            repository.addExercise(exercise)
        }
    }

    fun addExercises(exercises: List<Exercise>){
        viewModelScope.launch {
            repository.addExercises(exercises)
        }
    }

    fun updateExercises(exercises: List<Exercise>){
        viewModelScope.launch {
            repository.updateExercises(exercises)
        }
    }
    fun deleteExercise(exercise: Exercise){
        viewModelScope.launch{
            repository.deleteExercise(exercise)
        }
        _filteredExercises.value = _filteredExercises.value?.minus(exercise)
    }
    fun clearAndAddExercises(exercises: List<Exercise>){
        viewModelScope.launch{
            repository.clearAndAddExercises(exercises)
        }
    }

    private fun startTimer(milliseconds : Long) {
       timerCallback?.startTimer(milliseconds )
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
    fun filterExercisesByBodyPart(bodyPart: String) {
        val allExercises = exercises?.value ?: emptyList()
        val filteredExercises = allExercises.filter { it.bodyPart == bodyPart }
        _filteredExercises.value = filteredExercises
    }

}