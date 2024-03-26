package com.example.fitplan.model

data class Plan(
    val id : String = "",
    val title: String = "",
    val description : String = "",
    val image: Int = 0,
    val exercises: List<Exercise>
)  {
    // Add a default (no-argument) constructor
    constructor() : this("", "", "", 0, mutableListOf())
}