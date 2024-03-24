package com.example.fitplan

import com.example.fitplan.model.Exercise

class ExercisesData {

    val exercisesByBodyPart: Map<String, List<Exercise>> = mapOf(
        "Chest" to listOf(
            Exercise("Dumbbell Pullover", "Strengthens chest, shoulders, and triceps with a classic dumbbell exercise.", "Chest", R.drawable.dumbbell_pullover),
            Exercise("High Cable Crossover", "Targets chest and back muscles using cable equipment.", "Chest", R.drawable.high_cable_crossover),
            Exercise("Incline Chest Fly Machine", "Targets upper chest muscles with machine assistance.", "Chest", R.drawable.incline_chest_fly_machine),
            Exercise("Low Cable Crossover", "Focuses on lower chest muscles using cable equipment.", "Chest", R.drawable.low_cable_crossover),
            Exercise("Bench Press", "Strengthens chest muscles using a barbell on a bench.", "Chest",R.drawable.standing_medicine_ball_chest_pass),
            Exercise("Smith Machine Behind Neck Press", "Smith Machine Behind Neck Presses target the deltoids and upper chest.", "Chest",R.drawable.smith_machine_behind_neck_press),
            Exercise("Standing Dumbbell Overhead Press", "Standing Dumbbell Overhead Presses target the deltoids and triceps.", "Chest",R.drawable.standing_dumbbell_overhead_press)
            // Add more chest exercises here
        ),
        "Back" to listOf(
            Exercise("Cable Rear Pulldown", "Strengthens back muscles using cable equipment.", "Back", R.drawable.cable_rear_pulldown),
            Exercise("Front Pulldown", "Targets back, legs, and core with a compound exercise.", "Back", R.drawable.front_pulldown),
            Exercise("Lever T-Bar Row", "Works back muscles with a T-bar and lever system.", "Back",R.drawable.lever_tbar_row),
            Exercise("Pull Up", "Strengthens back muscles with a bodyweight movement.", "Back",R.drawable.pull_up),
            Exercise("Rope Climb", "Builds upper body strength with a climbing motion.", "Back",R.drawable.rope_climb),
            Exercise("Rowing Machine", "Simulates rowing for a full-body workout.", "Back",R.drawable.rowing_machine),
            Exercise("Seated Cable Row", "Targets mid-back muscles using a cable machine.", "Back",R.drawable.seated_cable_row),
            Exercise("Double Arm Dumbbell Curl", "Strengthens biceps using dumbbells.", "Back",R.drawable.double_arm_dumbbell_curl),
            Exercise("Dumbbell Curl", "Isolation exercise for biceps with dumbbells.", "Back",R.drawable.dumbbell_curl),
            Exercise("Seated Zottman Curl", "Targets biceps and forearms with a seated position.", "Back",R.drawable.seated_zottman_curl),
            Exercise("Standing Barbell Concentration Curl", "Focuses on biceps using a barbell and concentration.", "Back",R.drawable.standing_barbell_concentration_curl),
            Exercise("Waiter Curl", "Works biceps and forearms with a single-arm movement.", "Back",R.drawable.waiter_curl),
            Exercise("Dumbbell Kickback", "Dumbbell Kickbacks are an isolation exercise that targets the triceps.", "Back",R.drawable.dumbbell_kickback),
            Exercise("One Arm Reverse Push Down", "One Arm Reverse Push Downs are a unilateral exercise for targeting the triceps.", "Back",R.drawable.one_arm_reverse_push_down),
            Exercise("One Arm Triceps Pushdown", "One Arm Triceps Pushdowns are a unilateral exercise that isolates the triceps.", "Back",R.drawable.one_arm_triceps_pushdown),
            Exercise("Dumbbell Lateral Raise", "Dumbbell Lateral Raises target the lateral deltoid and help build shoulder width.", "Back",R.drawable.dumbbell_lateral_raise),
            Exercise("Dumbbell Shoulder Press", "Dumbbell Shoulder Presses are a compound exercise that primarily targets the deltoids.", "Back",R.drawable.dumbbell_shoulder_press),
            // Add more back exercises here
        ),
        "Legs" to listOf(
            Exercise("Depth Jump To Hurdle Hop", "Engages leg muscles with a dynamic plyometric exercise.", "Legs", R.drawable.depth_jump_to_hurdle_hop),
            Exercise("Dumbbell Lunges", "Dumbbell Lunges are a unilateral leg exercise that targets the quadriceps, hamstrings, and glutes.", "Legs",R.drawable.dumbbell_lunges),
            Exercise("High Knee Lunge on Bosu Ball", "High Knee Lunges on a Bosu Ball challenge balance and engage leg muscles.", "Legs",R.drawable.high_knee_lunge_on_bosu_ball),
            Exercise("Standing Leg Circles", "Standing Leg Circles are a dynamic stretching exercise for the lower body.", "Legs",R.drawable.standing_leg_circles)
            // Add more leg exercises here
        ),
        "Abs" to listOf(
            Exercise("Ab Coaster Machine", "Strengthens core muscles with the Ab Coaster Machine exercise.", "Abs", R.drawable.ab_coaster_machine),
            Exercise("Alternate Leg Raises", "Targets abdominals and hip flexors with alternating leg raises.", "Abs", R.drawable.alternate_leg_raises),
            Exercise("Crunch", "Crunches are a fundamental abdominal exercise that focuses on the rectus abdominis.", "Abs",R.drawable.crunch),
            Exercise("Leg Raise (Dragon Flag)", "The Leg Raise, also known as the Dragon Flag, is an advanced abdominal exercise that targets the entire core.", "Abs",R.drawable.leg_raise_dragon_flag),
            Exercise("Seated Bench Leg Pull In", "Seated Bench Leg Pull Ins are an effective exercise for targeting the lower abdominals.", "Abs",R.drawable.seated_bench_leg_pull_in)
            // Add more abs exercises here
        ),
    )
}