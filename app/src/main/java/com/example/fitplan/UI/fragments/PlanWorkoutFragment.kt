package com.example.fitplan.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplan.ExercisesData
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.adapters.PlanExerciseAdapter
import com.example.fitplan.databinding.FragmentPlanWorkoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class PlanWorkoutFragment : Fragment() {
    private var _binding: FragmentPlanWorkoutBinding? = null
    private val binding get() = _binding!!

    private val _mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mAuth get() = _mAuth

    private val viewModel : ExercisesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanWorkoutBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseData = ExercisesData()
        val allExercises = exerciseData.exercisesByBodyPart.values.flatten()
        val exerciseAdapter = PlanExerciseAdapter(allExercises, exerciseListener, viewModel)

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = exerciseAdapter

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
            // Handle exercise click
        }

        override fun onExerciseLongClicked(index: Int) {
            // Handle exercise long click
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}