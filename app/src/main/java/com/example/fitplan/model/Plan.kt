package com.example.fitplan.model

import android.net.Uri

data class Plan(
    val id : String = "",
    val title: String = "",
    val description : String = "",
    val image: Uri = Uri.EMPTY,
    val exercises: List<Exercise>
)
