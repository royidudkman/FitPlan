package com.example.fitplan.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.adapters.MyExerciseAdapter
import com.example.fitplan.databinding.FragmentMyWorkoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class MyWorkoutFragment : Fragment() {

    private var _binding: FragmentMyWorkoutBinding? = null
    private val binding get() = _binding!!

    private val _mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mAuth get() = _mAuth

    private val viewModel: ExercisesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyWorkoutBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.exercises?.observe(viewLifecycleOwner) { exercises ->
            binding.recycler.adapter = MyExerciseAdapter(exercises, exerciseListener, viewModel)
            viewModel.filterExercisesByBodyPart("Back")
        }

        viewModel.filteredExercises.observe(viewLifecycleOwner) { filteredExercises ->
            (binding.recycler.adapter as? MyExerciseAdapter)?.updateExercises(filteredExercises)
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        }


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text.toString()) {
                    "Back" -> filterExercises("Back")
                    "Chest" -> filterExercises("Chest")
                    "ABS" -> filterExercises("Abs")
                    "Legs" -> filterExercises("Legs")
                    "Cardio" -> filterExercises("Cardio")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun filterExercises(bodyPart: String) {
        // Perform filtering based on body part
        viewModel.filterExercisesByBodyPart(bodyPart)
    }

    private val exerciseListener = object : MyExerciseAdapter.ExerciseListener {
        override fun onExerciseClicked(index: Int) {
            //viewModel.setExercise(index)
            // Handle exercise click
        }

        override fun onExerciseLongClicked(index: Int) {
//            val item = (binding.recycler.adapter as MyExerciseAdapter).exerciseAt(index)
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("This action will delete the exercise")
//                .setMessage("Are you sure you want to delete the exercise?")
//                .setPositiveButton("Yes") { dialog, which ->
//                    selectedMuscle = binding.muscleNavigation.selectedItemId
//                    viewModel.deleteExercise(item)
//
//                    Toast.makeText(requireContext(), "Exercise deleted", Toast.LENGTH_SHORT).show()
//                }
//                .setNegativeButton("No") { dialog, which ->
//                    dialog.dismiss()
//                }
//                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}