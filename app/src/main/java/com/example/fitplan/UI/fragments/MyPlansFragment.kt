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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.UI.login_register.LoginViewModel
import com.example.fitplan.adapters.MyExerciseAdapter
import com.example.fitplan.databinding.FragmentMyPlansBinding
import com.example.fitplan.model.Plan
import com.example.fitplan.repository.PlansRepositoryFirebase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase

class MyPlansFragment : Fragment() {
    private var _binding: FragmentMyPlansBinding? = null
    private val binding get() = _binding!!

    private val exerciseViewModel: ExercisesViewModel by activityViewModels()

    private lateinit var myExerciseAdapter: MyExerciseAdapter

    private val viewModel : MyPlansViewModel by viewModels{
        MyPlansViewModel.MyPlansViewModelFactory(AuthRepositoryFirebase(),PlansRepositoryFirebase())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPlansBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myExerciseAdapter = MyExerciseAdapter(emptyList(), exerciseListener, exerciseViewModel)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myExerciseAdapter
        }

        fetchPlansFromFirestore("userId") { plans ->
            plans?.let {
                myExerciseAdapter.updateExercises(plans.flatMap { plan -> plan.exercises })
                myExerciseAdapter.notifyDataSetChanged()
            }
        }
    }

    fun fetchPlansFromFirestore(userId: String, callback: (List<Plan>?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).collection("plans")
            .get()
            .addOnSuccessListener { documents ->
                val plans = mutableListOf<Plan>()
                for (document in documents) {
                    val plan = document.toObject(Plan::class.java)
                    plans.add(plan)
                }
                callback(plans)
            }
            .addOnFailureListener { e ->
                // Handle failure
                callback(null)
            }
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
                    exerciseViewModel.deleteExercise(item)
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