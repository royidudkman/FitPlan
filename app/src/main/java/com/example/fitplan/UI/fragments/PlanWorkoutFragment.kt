package com.example.fitplan.UI.fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fitplan.ExercisesData
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.adapters.MyExerciseAdapter
import com.example.fitplan.adapters.PlanExerciseAdapter
import com.example.fitplan.databinding.FragmentPlanWorkoutBinding
import com.example.fitplan.model.Exercise
import com.example.fitplan.repository.PlansRepositoryFirebase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase

class PlanWorkoutFragment : Fragment() {
    private var _binding: FragmentPlanWorkoutBinding? = null
    private val binding get() = _binding!!

    private val exerciseViewModel: ExercisesViewModel by activityViewModels()

    private val viewModel: MyPlansViewModel by viewModels {
        MyPlansViewModel.MyPlansViewModelFactory(
            AuthRepositoryFirebase(),
            PlansRepositoryFirebase()
        )
    }

    private lateinit var planExerciseAdapter: PlanExerciseAdapter
    private var exercisesToPlan: MutableList<Exercise> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanWorkoutBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE

        binding.savePlanBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Save")
                .setMessage("Do you want to save the current plan?")
                .setPositiveButton("Yes") { dialog, which ->
                    viewModel.addPlan("StamPlan","Ahla Plan Shebaolam",R.drawable.waiter_curl,exercisesToPlan!!)
                    Toast.makeText(requireContext(), "Plan saved", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val exerciseData = ExercisesData()
        val allExercises = exerciseData.exercisesByBodyPart.values.flatten()
        planExerciseAdapter = PlanExerciseAdapter(allExercises, exerciseListener, exerciseViewModel)

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = planExerciseAdapter

        binding.muscleNavigation.selectedItemId = R.id.backMuscle_btn
        filterExercises("Back")

        binding.muscleNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.backMuscle_btn -> filterExercises("Back")
                R.id.chestMuscle_btn -> filterExercises("Chest")
                R.id.absMuscle_btn -> filterExercises("Abs")
                R.id.LegsMuscle_btn -> filterExercises("Legs")
                R.id.cardio_Btn -> filterExercises("Cardio")
            }
            true
        }
    }

    private fun filterExercises(bodyPart: String) {
        val exercisesData = ExercisesData()
        val exercisesForBodyPart = exercisesData.exercisesByBodyPart[bodyPart] ?: emptyList()
        (binding.recycler.adapter as? PlanExerciseAdapter)?.setExercises(exercisesForBodyPart)
    }

    private val exerciseListener = object : PlanExerciseAdapter.ExerciseListener {
        override fun onExerciseClicked(index: Int) {
            val exercise = planExerciseAdapter.exerciseAt(index)
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val dialogView: View = layoutInflater.inflate(R.layout.plan_exercise_card_layout, null)
            setDialogUI(dialogView, exercise)
            val totalMilliseconds = IncreaseDecreaseUI(dialogView, exercise)
            //TODO check if the exercise is in my plan
            val addExerciseBtn = dialogView.findViewById<MaterialButton>(R.id.add_exercise_btn)
            val removeExerciseBtn =
                dialogView.findViewById<MaterialButton>(R.id.remove_exercise_btn)

            addExerciseBtn.setOnClickListener {
                val repsEditText = dialogView.findViewById<TextInputEditText>(R.id.reps_text)
                val repsText = repsEditText.text.toString()
                val repsValue: Int? = repsText.toIntOrNull()
                if (repsValue != null) {
                    exercise.reps = repsValue
                } else {
                    exercise.reps = 0
                }

                exercise.time = totalMilliseconds
                exercisesToPlan.add(exercise)
                Toast.makeText(requireContext(), "${exercise.name} added to your plan", Toast.LENGTH_SHORT).show()

            }

            removeExerciseBtn.setOnClickListener {

            }


            dialogBuilder.setView(dialogView)
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }

        override fun onExerciseLongClicked(index: Int) {
            TODO("Not yet implemented")
        }

    }


    fun setDialogUI(dialogView: View, exercise: Exercise) {

        val image = dialogView.findViewById<ImageView>(R.id.exercise_image)
        image.setImageResource(exercise.image)
        //Glide.with(image.context).asGif().load(exercise.image).into(image)

        val title = dialogView.findViewById<TextView>(R.id.title_tv)
        title.text = exercise.name

        val description = dialogView.findViewById<TextView>(R.id.description_tv)
        description.text = exercise.description


        val reps = dialogView.findViewById<TextInputEditText>(R.id.reps_text)

        val increaseMinutes = dialogView.findViewById<Button>(R.id.increase_minutes_btn)
        val decreaseMinutes = dialogView.findViewById<Button>(R.id.decrease_minutes_btn)
        val minutes = dialogView.findViewById<TextView>(R.id.minutes_tv)
        val seconds = dialogView.findViewById<TextView>(R.id.seconds_tv)

        val increaseSeconds = dialogView.findViewById<Button>(R.id.increase_second_btn)
        val decreaseSeconds = dialogView.findViewById<Button>(R.id.decrease_second_btn)
    }

    fun IncreaseDecreaseUI(dialogView: View, exercise: Exercise): Long {

        var totalMinutes = 0
        var totalSeconds = 0
        val increaseMinutes = dialogView.findViewById<Button>(R.id.increase_minutes_btn)
        val decreaseMinutes = dialogView.findViewById<Button>(R.id.decrease_minutes_btn)
        val minutesText = dialogView.findViewById<TextView>(R.id.minutes_tv)
        minutesText.text = "Minutes ${totalMinutes}"

        val increaseSeconds = dialogView.findViewById<Button>(R.id.increase_second_btn)
        val decreaseSeconds = dialogView.findViewById<Button>(R.id.decrease_second_btn)
        val secondsText = dialogView.findViewById<TextView>(R.id.seconds_tv)
        secondsText.text = "Seconds ${totalSeconds}"

        increaseMinutes.setOnClickListener {
            minutesText.text = "Minutes ${++totalMinutes}"
        }

        decreaseMinutes.setOnClickListener {
            if (totalMinutes == 0) totalMinutes = 1
            minutesText.text = "Minutes ${--totalMinutes}"
        }

        increaseSeconds.setOnClickListener {
            secondsText.text = "Seconds ${++totalSeconds}"
        }

        decreaseSeconds.setOnClickListener {
            if (totalSeconds == 0) totalSeconds = 1
            secondsText.text = "Seconds ${--totalSeconds}"
        }

        val totalMilliseconds = ((totalMinutes * 60 + totalSeconds) * 1000).toLong()
        return totalMilliseconds
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

