package com.example.fitplan.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
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

    private var selectedTabIndex = 0



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
            if (exercises != null) {
                // Set up adapter with filtered exercises
                viewModel.filteredExercises.value?.let { filteredExercises ->
                    binding.recycler.adapter = MyExerciseAdapter(filteredExercises, exerciseListener, viewModel)
                    binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                }
                // Select "Back" tab and filter exercises
                binding.tabs.getTabAt(selectedTabIndex)?.select() // Assuming "Back" is the first tab
                filterExercises(getBodyPartForTabIndex(selectedTabIndex))
            }
        }


        viewModel.filteredExercises.observe(viewLifecycleOwner) { filteredExercises ->
            binding.recycler.adapter = MyExerciseAdapter(filteredExercises, exerciseListener, viewModel)
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    selectedTabIndex = it.position // Update selectedTabIndex when a tab is selected
                    filterExercises(getBodyPartForTabIndex(it.position))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun getBodyPartForTabIndex(tabIndex: Int): String {
        return when (tabIndex) {
            1 -> "Chest"
            2 -> "Abs"
            3 -> "Legs"
            4 -> "Cardio"
            else -> "Back"
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