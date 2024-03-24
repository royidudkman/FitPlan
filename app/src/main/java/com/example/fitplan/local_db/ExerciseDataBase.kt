package com.example.fitplan.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitplan.model.Exercise

@Database(entities = arrayOf(Exercise::class), version = 2, exportSchema = false)
abstract class ExerciseDataBase : RoomDatabase() {
    abstract fun ExerciseDao(): ExerciseDao

    companion object{

        @Volatile
        private var instance: ExerciseDataBase? = null

        fun getDatabase(context: Context) = instance?: synchronized(this){
            Room.databaseBuilder(context.applicationContext, ExerciseDataBase::class.java, "exercises_db").build()
        }
    }


}