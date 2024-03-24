package com.example.fitplan.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "exercises")
data class Exercise(

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name = "description")
    val description : String,

    @ColumnInfo(name = "bodyPart")
    val bodyPart : String,

    @ColumnInfo(name = "image")
    val image : Int)


    : Parcelable {
        @PrimaryKey(autoGenerate = true)
        var id : Int = 0
    }





