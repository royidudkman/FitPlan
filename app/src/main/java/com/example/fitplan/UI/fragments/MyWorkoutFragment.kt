package com.example.fitplan.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.adapters.MyExerciseAdapter
import com.example.fitplan.databinding.FragmentMyWorkoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MyWorkoutFragment : Fragment() {

    private var _binding: FragmentMyWorkoutBinding? = null
    private val binding get() = _binding!!

    private val _mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mAuth get() = _mAuth

    private val viewModel : ExercisesViewModel by activityViewModels()

    private var isInitialFilterApplied = false
    private var selectedMuscle = R.id.backMuscle_btn
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyWorkoutBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupBottomNavigation()

        binding.muscleNavigation.selectedItemId = selectedMuscle

        viewModel.exercises?.observe(viewLifecycleOwner) { exercises ->
            binding.recycler.adapter = MyExerciseAdapter(exercises, exerciseListener, viewModel)
            filterExercises(binding.muscleNavigation.menu.findItem(selectedMuscle)?.title.toString())
        }

        viewModel.filteredExercises.observe(viewLifecycleOwner) { filteredExercises ->
            (binding.recycler.adapter as? MyExerciseAdapter)?.updateExercises(filteredExercises)
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        }

    }

    private fun setupBottomNavigation() {
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
        // Perform filtering based on body part
        viewModel.filterExercisesByBodyPart(bodyPart)
    }

    private val exerciseListener = object : MyExerciseAdapter.ExerciseListener {
        override fun onExerciseClicked(index: Int) {
            //viewModel.setExercise(index)
            // Handle exercise click
        }

        override fun onExerciseLongClicked(index: Int) {
            val item = (binding.recycler.adapter as MyExerciseAdapter).exerciseAt(index)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("This action will delete the exercise")
                .setMessage("Are you sure you want to delete the exercise?")
                .setPositiveButton("Yes") { dialog, which ->
                    selectedMuscle = binding.muscleNavigation.selectedItemId
                    viewModel.deleteExercise(item)

                    Toast.makeText(requireContext(), "Exercise deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}