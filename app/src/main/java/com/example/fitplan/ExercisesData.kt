package com.example.fitplan

import android.content.Context
import com.example.fitplan.model.Exercise

class ExercisesData(private val context: Context) {

    val exercisesByBodyPart: Map<String, List<Exercise>> = mapOf(
        "Chest" to listOf(
            Exercise(context.getString(R.string.dumbbell_pullover),context.getString(R.string.strengthens_chest_shoulders_and_triceps_with_a_classic_dumbbell_exercise), "Chest", R.drawable.dumbbell_pullover),
            Exercise(context.getString(R.string.high_cable_crossover),context.getString(R.string.targets_chest_and_back_muscles_using_cable_equipment), "Chest", R.drawable.high_cable_crossover),
            Exercise(context.getString(R.string.incline_chest_fly_machine), context.getString(R.string.targets_upper_chest_muscles_with_machine_assistance), "Chest", R.drawable.incline_chest_fly_machine),
            Exercise(context.getString(R.string.low_cable_crossover), context.getString(R.string.focuses_on_lower_chest_muscles_using_cable_equipment), "Chest", R.drawable.low_cable_crossover),
            Exercise(context.getString(R.string.bench_press), context.getString(R.string.strengthens_chest_muscles_using_a_barbell_on_a_bench), "Chest",R.drawable.standing_medicine_ball_chest_pass),
            Exercise(context.getString(R.string.smith_machine_behind_neck_press), context.getString(R.string.smith_machine_behind_neck_presses_target_the_deltoids_and_upper_chest), "Chest",R.drawable.smith_machine_behind_neck_press),
            Exercise(context.getString(R.string.standing_dumbbell_overhead_press), context.getString(R.string.standing_dumbbell_overhead_presses_target_the_deltoids_and_triceps), "Chest",R.drawable.standing_dumbbell_overhead_press)

        ),
        "Back" to listOf(
            Exercise(context.getString(R.string.cable_rear_pulldown), context.getString(R.string.strengthens_back_muscles_using_cable_equipment), "Back", R.drawable.cable_rear_pulldown),
            Exercise(context.getString(R.string.front_pulldown), context.getString(R.string.targets_back_legs_and_core_with_a_compound_exercise), "Back", R.drawable.front_pulldown),
            Exercise(context.getString(R.string.lever_t_bar_row), context.getString(R.string.works_back_muscles_with_a_t_bar_and_lever_system), "Back",R.drawable.lever_tbar_row),
            Exercise(context.getString(R.string.pull_up), context.getString(R.string.strengthens_back_muscles_with_a_bodyweight_movement), "Back",R.drawable.pull_up),
            Exercise(context.getString(R.string.rope_climb), context.getString(R.string.builds_upper_body_strength_with_a_climbing_motion), "Back",R.drawable.rope_climb),
            Exercise(context.getString(R.string.rowing_machine), context.getString(R.string.simulates_rowing_for_a_full_body_workout), "Back",R.drawable.rowing_machine),
            Exercise(context.getString(R.string.seated_cable_row), context.getString(R.string.targets_mid_back_muscles_using_a_cable_machine), "Back",R.drawable.seated_cable_row),
            Exercise(context.getString(R.string.double_arm_dumbbell_curl), context.getString(R.string.strengthens_biceps_using_dumbbells), "Back",R.drawable.double_arm_dumbbell_curl),
            Exercise(context.getString(R.string.dumbbell_curl), context.getString(R.string.isolation_exercise_for_biceps_with_dumbbells), "Back",R.drawable.dumbbell_curl),
            Exercise(context.getString(R.string.seated_zottman_curl), context.getString(R.string.targets_biceps_and_forearms_with_a_seated_position), "Back",R.drawable.seated_zottman_curl),
            Exercise(context.getString(R.string.standing_barbell_concentration_curl), context.getString(R.string.focuses_on_biceps_using_a_barbell_and_concentration), "Back",R.drawable.standing_barbell_concentration_curl),
            Exercise(context.getString(R.string.waiter_curl), context.getString(R.string.works_biceps_and_forearms_with_a_single_arm_movement), "Back",R.drawable.waiter_curl),
            Exercise(context.getString(R.string.dumbbell_kickback), context.getString(R.string.dumbbell_kickbacks_are_an_isolation_exercise_that_targets_the_triceps), "Back",R.drawable.dumbbell_kickback),
            Exercise(context.getString(R.string.one_arm_reverse_push_down), context.getString(R.string.one_arm_reverse_push_downs_are_a_unilateral_exercise_for_targeting_the_triceps), "Back",R.drawable.one_arm_reverse_push_down),
            Exercise(context.getString(R.string.one_arm_triceps_pushdown), context.getString(R.string.one_arm_triceps_pushdowns_are_a_unilateral_exercise_that_isolates_the_triceps), "Back",R.drawable.one_arm_triceps_pushdown),
            Exercise(context.getString(R.string.dumbbell_lateral_raise), context.getString(R.string.dumbbell_lateral_raises_target_the_lateral_deltoid_and_help_build_shoulder_width), "Back",R.drawable.dumbbell_lateral_raise),
            Exercise(context.getString(R.string.dumbbell_shoulder_press), context.getString(R.string.dumbbell_shoulder_presses_are_a_compound_exercise_that_primarily_targets_the_deltoids), "Back",R.drawable.dumbbell_shoulder_press),

        ),
        "Legs" to listOf(
            Exercise(context.getString(R.string.depth_jump_to_hurdle_hop), context.getString(R.string.engages_leg_muscles_with_a_dynamic_plyometric_exercise), "Legs", R.drawable.depth_jump_to_hurdle_hop),
            Exercise(context.getString(R.string.dumbbell_lunges), context.getString(R.string.dumbbell_lunges_are_a_unilateral_leg_exercise_that_targets_the_quadriceps_hamstrings_and_glutes), "Legs",R.drawable.dumbbell_lunges),
            Exercise(context.getString(R.string.high_knee_lunge_on_bosu_ball), context.getString(R.string.high_knee_lunges_on_a_bosu_ball_challenge_balance_and_engage_leg_muscles), "Legs",R.drawable.high_knee_lunge_on_bosu_ball),
            Exercise(context.getString(R.string.standing_leg_circles), context.getString(R.string.standing_leg_circles_are_a_dynamic_stretching_exercise_for_the_lower_body), "Legs",R.drawable.standing_leg_circles)

        ),
        "Abs" to listOf(
            Exercise(context.getString(R.string.ab_coaster_machine), context.getString(R.string.strengthens_core_muscles_with_the_ab_coaster_machine_exercise), "Abs", R.drawable.ab_coaster_machine),
            Exercise(context.getString(R.string.alternate_leg_raises), context.getString(R.string.targets_abdominals_and_hip_flexors_with_alternating_leg_raises), "Abs", R.drawable.alternate_leg_raises),
            Exercise(context.getString(R.string.crunch), context.getString(R.string.crunches_are_a_fundamental_abdominal_exercise_that_focuses_on_the_rectus_abdominis), "Abs",R.drawable.crunch),
            Exercise(context.getString(R.string.leg_raise_dragon_flag), context.getString(R.string.the_leg_raise_also_known_as_the_dragon_flag_is_an_advanced_abdominal_exercise_that_targets_the_entire_core), "Abs",R.drawable.leg_raise_dragon_flag),
            Exercise(context.getString(R.string.seated_bench_leg_pull_in), context.getString(R.string.seated_bench_leg_pull_ins_are_an_effective_exercise_for_targeting_the_lower_abdominals), "Abs",R.drawable.seated_bench_leg_pull_in)

        ),
        "Cardio" to listOf(
            Exercise(context.getString(R.string.briskly_walking), context.getString(R.string.strengthens_core_muscles_with_brisk_walking), "Cardio", R.drawable.briskly_walking),
            Exercise(context.getString(R.string.burpees), context.getString(R.string.targets_abdominals_and_hip_flexors_with_burpees), "Cardio", R.drawable.burpees),
            Exercise(context.getString(R.string.high_knee_skips), context.getString(R.string.high_knee_skips_are_a_cardiovascular_exercise_focusing_on_the_rectus_abdominis), "Cardio", R.drawable.high_knee_skips),
            Exercise(context.getString(R.string.jump_squat), context.getString(R.string.jump_squats_target_the_entire_core_while_engaging_leg_muscles), "Cardio", R.drawable.jump_squat),
            Exercise(context.getString(R.string.mountain_climber), context.getString(R.string.mountain_climbers_engage_the_core_and_provide_a_cardio_workout), "Cardio", R.drawable.mountain_climber),
            Exercise(context.getString(R.string.run), context.getString(R.string.running_engages_the_entire_body_including_the_core_muscles), "Cardio", R.drawable.run),
            Exercise(context.getString(R.string.skater), context.getString(R.string.skaters_engage_the_core_muscles_while_providing_a_cardio_workout), "Cardio", R.drawable.skater),
            Exercise(context.getString(R.string.walking), context.getString(R.string.walking_is_a_low_impact_exercise_that_strengthens_core_muscles), "Cardio", R.drawable.walking)

        )

    )
}