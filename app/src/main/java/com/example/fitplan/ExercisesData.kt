package com.example.fitplan

import com.example.fitplan.model.Exercise

class ExercisesData {

    val exercisesByBodyPart: Map<String, List<Exercise>> = mapOf(
        "Chest" to listOf(
            Exercise("Push ups", "A classic bodyweight exercise targeting chest, shoulders, and triceps.", "Chest", R.drawable.dumbbell_pullover),
            Exercise("Dumbbell Pullover", "Strengthens chest and back muscles with a dumbbell.", "Chest", R.drawable.high_cable_crossover),
            Exercise("Incline Chest Fly Machine", "Targets upper chest muscles with machine assistance.", "Chest", R.drawable.incline_chest_fly_machine),
            Exercise("Low Cable Crossover", "Focuses on lower chest muscles using cable equipment.", "Chest", R.drawable.low_cable_crossover),
            Exercise("Bench Press", "Strengthens chest muscles using a barbell on a bench.", "Chest",R.drawable.standing_medicine_ball_chest_pass)
            // Add more chest exercises here
        ),
        "Back" to listOf(
            Exercise("Pull ups", "A bodyweight exercise that strengthens back muscles.", "Back",R.drawable.cable_rear_pulldown),
            Exercise("Deadlift", "Compound exercise targeting back, legs, and core.", "Back",R.drawable.front_pulldown),
            Exercise("Lever T-Bar Row", "Works back muscles with a T-bar and lever system.", "Back",R.drawable.lever_tbar_row),
            Exercise("Pull Up", "Strengthens back muscles with a bodyweight movement.", "Back",R.drawable.pull_up),
            Exercise("Rope Climb", "Builds upper body strength with a climbing motion.", "Back",R.drawable.rope_climb),
            Exercise("Rowing Machine", "Simulates rowing for a full-body workout.", "Back",R.drawable.rowing_machine),
            Exercise("Seated Cable Row", "Targets mid-back muscles using a cable machine.", "Back",R.drawable.seated_cable_row),
            Exercise("Double Arm Dumbbell Curl", "Strengthens biceps using dumbbells.", "Back",R.drawable.double_arm_dumbbell_curl),
            Exercise("Dumbbell Curl", "Isolation exercise for biceps with dumbbells.", "Back",R.drawable.dumbbell_curl),
            Exercise("Seated Zottman Curl", "Targets biceps and forearms with a seated position.", "Back",R.drawable.seated_zottman_curl),
            Exercise("Standing Barbell Concentration Curl", "Focuses on biceps using a barbell and concentration.", "Back",R.drawable.standing_barbell_concentration_curl),
            Exercise("Waiter Curl", "Works biceps and forearms with a single-arm movement.", "Back",R.drawable.waiter_curl)
            // Add more back exercises here
        ),
        "Legs" to listOf(
            Exercise("Squats", "A compound exercise targeting legs, glutes, and core.", "Legs",1),
            Exercise("Jump", "Plyometric exercise for explosive leg power.", "Legs",1)
            // Add more leg exercises here
        ),
        "Abs" to listOf(
            Exercise("Sit ups", "Classic ab exercise for core strength.", "Abs",R.drawable.ab_coaster_machine),
            Exercise("L Sit", "Challenging core exercise targeting abs and hip flexors.", "Abs",R.drawable.alternate_leg_raises)
            // Add more abs exercises here
        ),
        "Cardio" to listOf(
            Exercise("Run", "Aerobic exercise for cardiovascular health.", "Cardio",1),
            Exercise("Skip Rope", "Effective cardio workout using a jump rope.", "Cardio",1)
            // Add more cardio exercises here
        )
    )
}
