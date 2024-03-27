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

    interface TimerCallback {
        fun startTimer(textView: TextView, milliseconds: Long)
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
    fun deleteExercise(exercise: Exercise){
        viewModelScope.launch{
            repository.deleteExercise(exercise)
        }
        _filteredExercises.value = _filteredExercises.value?.minus(exercise)
    }

    private fun startTimer(textView: TextView, milliseconds : Long) { //TODO : ADD TO THE DATA THE TIME THAT THE USER WANT
       timerCallback?.startTimer(textView, milliseconds )
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
    fun filterExercisesByBodyPart(bodyPart: String) {
        val allExercises = exercises?.value
        if (allExercises != null) {
            val filteredExercises = allExercises.filter { it.bodyPart == bodyPart }
            _filteredExercises.value = filteredExercises
        }
    }

}