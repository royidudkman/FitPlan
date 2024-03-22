package com.example.fitplan

import com.example.fitplan.model.Exercise

class ExercisesData {

    val exercisesByBodyPart: Map<String, List<Exercise>> = mapOf(
        "Chest" to listOf(
            Exercise("Exercise 1", "Description 1", 1),
            Exercise("Exercise 2", "Description 2", 1)
            // Add more chest exercises here
        ),
        "Back" to listOf(
            Exercise("Exercise 1", "Description 1", 1),
            Exercise("Exercise 2", "Description 2", 1)
            // Add more back exercises here
        ),
        "Legs" to listOf(
            Exercise("Exercise 1", "Description 1", 1),
            Exercise("Exercise 2", "Description 2", 1)
            // Add more leg exercises here
        ),
        "Abs" to listOf(
            Exercise("Exercise 1", "Description 1", 1),
            Exercise("Exercise 2", "Description 2", 1)
            // Add more abs exercises here
        ),
        "Cardio" to listOf(
            Exercise("Exercise 1", "Description 1", 1),
            Exercise("Exercise 2", "Description 2", 1)
            // Add more cardio exercises here
        )
    )
}