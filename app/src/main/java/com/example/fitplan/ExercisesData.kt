package com.example.fitplan

import com.example.fitplan.model.Exercise

class ExercisesData {

    val exercisesByBodyPart: Map<String, List<Exercise>> = mapOf(
        "Chest" to listOf(
            Exercise("Push ups", "Description 1", 1),
            Exercise("bench press", "Description 2", 1)
            // Add more chest exercises here
        ),
        "Back" to listOf(
            Exercise("Pull ups", "Description 1", 1),
            Exercise("Dead lift", "Description 2", 1)
            // Add more back exercises here
        ),
        "Legs" to listOf(
            Exercise("squats", "Description 1", 1),
            Exercise("jump", "Description 2", 1)
            // Add more leg exercises here
        ),
        "Abs" to listOf(
            Exercise("sit ups", "Description 1", 1),
            Exercise("L Sit", "Description 2", 1)
            // Add more abs exercises here
        ),
        "Cardio" to listOf(
            Exercise("Run", "Description 1", 1),
            Exercise("skip rope", "Description 2", 1)
            // Add more cardio exercises here
        )
    )
}