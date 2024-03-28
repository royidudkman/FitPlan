package com.example.fitplan.UI.fragments

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fitplan.ExercisesData
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.PickImageViewModel
import com.example.fitplan.R
import com.example.fitplan.SharedViewModel
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
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val viewModel: MyPlansViewModel by viewModels {
        MyPlansViewModel.MyPlansViewModelFactory(
            AuthRepositoryFirebase(),
            PlansRepositoryFirebase()
        )
    }

    private val pickImageViewModel : PickImageViewModel by viewModels()
    val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { pickedUri ->
            pickImageViewModel.setPhotoURi(pickedUri)
        }
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

            val dialogBuilder = AlertDialog.Builder(requireContext())
            val dialogView: View = layoutInflater.inflate(R.layout.create_plan_card_layout, null)
            dialogBuilder.setView(dialogView)
            val alertDialog = dialogBuilder.create()

            val titleText = dialogView.findViewById<TextInputEditText>(R.id.title_text)
            val descriptionText = dialogView.findViewById<TextInputEditText>(R.id.description_text)
            val chooseImageBtn = dialogView.findViewById<Button>(R.id.chooseImage_btn)
            val planImage = dialogView.findViewById<ImageView>(R.id.planImage)
            val saveBtn = dialogView.findViewById<Button>(R.id.save_btn)
            val cancelBtn = dialogView.findViewById<Button>(R.id.cancel_btn)
            var chosenImage : Uri = Uri.EMPTY

            chooseImageBtn.setOnClickListener{
                pickImageLauncher.launch("image/*")

                pickImageViewModel.photoUri.observe(viewLifecycleOwner){uri ->
                    uri?.let { pickedUri ->
                        planImage.setImageURI(pickedUri)
                        chosenImage = pickedUri
                    }
                }
            }

            sharedViewModel.exerciseToPlan.observe(viewLifecycleOwner) { exercises ->

                saveBtn.setOnClickListener {
                    viewModel.addPlan(
                        titleText.text.toString(),
                        descriptionText.text.toString(),
                        chosenImage,//TODO get image from phone
                        exercises
                    )
                    Toast.makeText(requireContext(), "Plan saved", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            cancelBtn.setOnClickListener {
                alertDialog.dismiss()
            }


            alertDialog.show()

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
            sharedViewModel.setSelectedExercise(exercise)
            findNavController().navigate(R.id.action_planWorkoutFragment2_to_planExerciseCardFragment)

        }

        override fun onExerciseLongClicked(index: Int) {
            TODO("Not yet implemented")
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

