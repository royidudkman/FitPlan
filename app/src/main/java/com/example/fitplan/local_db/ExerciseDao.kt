package com.example.fitplan.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitplan.model.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExercises(exercises: List<Exercise>)

    @Update
    suspend fun updateExercises(newExercises: List<Exercise>)

    @Delete
    suspend fun deleteExercise(vararg exercises:Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getExercises() : LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id LIKE :id")
    fun getExercise(id : Int) : Exercise

    @Query("DELETE FROM exercises")
    fun deleteAll()

    @Query("SELECT * FROM exercises WHERE bodyPart = :bodyPart ORDER BY name ASC")
    fun getExercisesByBodyPart(bodyPart: String): LiveData<List<Exercise>>
}