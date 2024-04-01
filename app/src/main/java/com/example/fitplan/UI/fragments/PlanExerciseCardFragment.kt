package com.example.fitplan.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.fitplan.R
import com.example.fitplan.view_models.SharedViewModel
import com.example.fitplan.adapters.PlanExerciseAdapter
import com.example.fitplan.databinding.PlanExerciseCardLayoutBinding
import com.example.fitplan.model.Exercise
import com.google.android.material.bottomnavigation.BottomNavigationView


class PlanExerciseCardFragment : Fragment() {
    private var _binding: PlanExerciseCardLayoutBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel : SharedViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlanExerciseCardLayoutBinding.inflate(inflater, container, false)

        sharedViewModel.selectedExercise.observe(viewLifecycleOwner) { exercise ->

            Glide.with(requireContext()).asGif().load(exercise.image).into(binding.exerciseImage)
            binding.titleTv.text = exercise.name
            binding.descriptionTv.text = exercise.description



            binding.addExerciseBtn.setOnClickListener {

                exercise.reps = binding.repsText.text.toString().toIntOrNull() ?: 0

                var minutes = sharedViewModel.totalMinutes
                var seconds = sharedViewModel.totalSeconds
                val totalMilliseconds = ((minutes * 60) + seconds) * 1000L
                exercise.time = totalMilliseconds

                exercise.generateId()

                sharedViewModel.addExerciseToPlan(exercise)
                Toast.makeText(requireContext(), " ${exercise.name} " + getString(R.string.added_to_your_plan), Toast.LENGTH_SHORT).show()

            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE

        binding.minutesTv.text = getString(R.string.minutes) + " ${sharedViewModel.totalMinutes}"
        binding.secondsTv.text = getString(R.string.seconds) + " ${sharedViewModel.totalSeconds}"


        binding.increaseMinutesBtn.setOnClickListener {
            sharedViewModel.totalMinutes++
            updateUIText()
        }

        binding.decreaseMinutesBtn.setOnClickListener {
            if (sharedViewModel.totalMinutes > 0) {
                sharedViewModel.totalMinutes--
                updateUIText()
            }
        }

        binding.increaseSecondBtn.setOnClickListener {
            sharedViewModel.totalSeconds++
            updateUIText()
        }

        binding.decreaseSecondBtn.setOnClickListener {
            if (sharedViewModel.totalSeconds > 0) {
                sharedViewModel.totalSeconds--
                updateUIText()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateUIText() {
        binding.minutesTv.text = getString(R.string.minutes) + " ${sharedViewModel.totalMinutes}"
        binding.secondsTv.text = getString(R.string.seconds) + " ${sharedViewModel.totalSeconds}"
    }


}