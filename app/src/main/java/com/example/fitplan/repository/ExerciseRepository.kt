package com.example.fitplan.repository

import android.app.Application
import com.example.fitplan.local_db.ExerciseDao
import com.example.fitplan.local_db.ExerciseDataBase
import com.example.fitplan.model.Exercise
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExerciseRepository(application: Application) {

    private var exerciseDao : ExerciseDao?

    init{
        val db = ExerciseDataBase.getDatabase(application.applicationContext)
        exerciseDao = db?.ExerciseDao() // should be exerciseDao
    }

    fun getExercises() = exerciseDao?.getExercises()

    suspend fun addExercise(exercise: Exercise){
        exerciseDao?.addExercise(exercise)
    }

    suspend fun addExercises(exercises: List<Exercise>){
        exerciseDao?.addExercises(exercises)
    }

    suspend fun updateExercises(newExercises: List<Exercise>){
        exerciseDao?.updateExercises(newExercises)
    }
    suspend fun deleteExercise(exercise: Exercise){
        exerciseDao?.deleteExercise(exercise)
    }
    suspend fun clearAndAddExercises(exercises: List<Exercise>){
        deleteAll()
        addExercises(exercises)
    }
    fun getExercise(id:Int) = exerciseDao?.getExercise(id)
    suspend fun deleteAll(){
        withContext(Dispatchers.IO){
            exerciseDao?.deleteAll()
        }
    }

}