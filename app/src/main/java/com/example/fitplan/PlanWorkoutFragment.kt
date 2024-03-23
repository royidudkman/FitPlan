package com.example.fitplan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

        val exerciseData = ExercisesData()

        val allExercises = exerciseData.exercisesByBodyPart.values.flatten()
        binding.recycler.adapter = PlanExerciseAdapter(allExercises, object : PlanExerciseAdapter.ExerciseListener {
            override fun onExerciseClicked(index: Int) {
                // Handle exercise item clicked
            }

            override fun onExerciseLongClicked(index: Int) {
                Toast.makeText(requireContext(), "${allExercises[index]}", Toast.LENGTH_SHORT).show()
            }
        }, viewModel)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}