package com.example.fitplan.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.SharedViewModel
import com.example.fitplan.adapters.MyExerciseAdapter
import com.example.fitplan.databinding.FragmentMySocialWorkoutBinding
import com.example.fitplan.databinding.FragmentMyWorkoutBinding
import com.example.fitplan.model.Exercise
import com.example.fitplan.repository.PlansRepositoryFirebase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase


class MySocialWorkoutFragment : Fragment() {
    private var _binding: FragmentMySocialWorkoutBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val viewModel: ExercisesViewModel by activityViewModels()
    private lateinit var myExerciseAdapter: MyExerciseAdapter

    private var currentTab = "Back"
    private val exercisesByBodyPart = mutableMapOf<String, List<Exercise>>()

    private lateinit var planId : String

    private val myPlansviewModel : MyPlansViewModel by viewModels{
        MyPlansViewModel.MyPlansViewModelFactory(
            AuthRepositoryFirebase(),
            PlansRepositoryFirebase()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMySocialWorkoutBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myExerciseAdapter = MyExerciseAdapter(emptyList(), exerciseListener, viewModel)





        sharedViewModel.selectedPlan.observe(viewLifecycleOwner) { plan ->
            planId = plan.id

            plan.exercises?.let { exercises ->
                categorizeExercises(exercises)
                viewModel.clearAndAddExercises(exercises ?: emptyList())
                myExerciseAdapter.updateExercises(exercisesByBodyPart[currentTab] ?: emptyList())
                binding.tabs.getTabAt(getTabIndexForBodyPart(currentTab))?.select()
                binding.recycler.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = myExerciseAdapter
                }
            }
        }

        viewModel.exercises?.observe(viewLifecycleOwner){exercises ->
            categorizeExercises(exercises)
            //myExerciseAdapter.updateExercises(exercisesByBodyPart[currentTab] ?: emptyList())
            //binding.tabs.getTabAt(getTabIndexForBodyPart(currentTab))?.select()
            binding.recycler.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = myExerciseAdapter
            }
        }


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val bodyPart = getBodyPartForTabIndex(it.position)

                    myExerciseAdapter.updateExercises(exercisesByBodyPart[bodyPart] ?: emptyList())
                    binding.recycler.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = myExerciseAdapter
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun categorizeExercises(exercises: List<Exercise>) {
        exercisesByBodyPart.clear()
        exercisesByBodyPart["Back"] = exercises.filter { it.bodyPart == "Back" }
        exercisesByBodyPart["Chest"] = exercises.filter { it.bodyPart == "Chest" }
        exercisesByBodyPart["Legs"] = exercises.filter { it.bodyPart == "Legs" }
        exercisesByBodyPart["Abs"] = exercises.filter { it.bodyPart == "Abs" }
        exercisesByBodyPart["Cardio"] = exercises.filter { it.bodyPart == "Cardio" }
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

    private fun getTabIndexForBodyPart(bodyPart: String): Int {
        return when (bodyPart) {
            "Chest" -> 1
            "Abs" -> 2
            "Legs" -> 3
            "Cardio" -> 4
            else -> 0 // Default to "Back"
        }
    }


    private val exerciseListener = object : MyExerciseAdapter.ExerciseListener {
        override fun onExerciseClicked(index: Int) {
            val item = (binding.recycler.adapter as MyExerciseAdapter).exerciseAt(index)
            currentTab = item.bodyPart
            sharedViewModel.setSelectedExercise(item)
            findNavController().navigate(R.id.action_mySocialWorkoutFragment2_to_myExerciseCardFragment)

        }

        override fun onExerciseLongClicked(index: Int) {

        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}