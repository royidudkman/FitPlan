package com.example.fitplan.repository

import android.app.Application
import com.example.fitplan.local_db.ExerciseDao
import com.example.fitplan.local_db.ExerciseDataBase
import com.example.fitplan.model.Exercise

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
    suspend fun deleteExercise(exercise: Exercise){
        exerciseDao?.deleteExercise(exercise)
    }
    fun getExercise(id:Int) = exerciseDao?.getExercise(id)
    fun deleteAll(){
        exerciseDao?.deleteAll()
    }

}