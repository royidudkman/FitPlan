package com.example.fitplan.model

import android.graphics.Bitmap
import android.net.Uri

data class Plan(
    val id : String = "",
    val title: String = "",
    val description : String = "",
    val imageString: String = "",
    val bitmap : Bitmap? = null,
    val exercises: List<Exercise>
)
