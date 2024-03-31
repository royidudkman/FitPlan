package com.example.fitplan.UI.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplan.ExercisesData
import com.example.fitplan.view_models.ExercisesViewModel
import com.example.fitplan.view_models.PickImageViewModel
import com.example.fitplan.R
import com.example.fitplan.UI.fragments.my_plans.MyPlansViewModel
import com.example.fitplan.view_models.SharedViewModel
import com.example.fitplan.adapters.PlanExerciseAdapter
import com.example.fitplan.databinding.FragmentPlanWorkoutBinding
import com.example.fitplan.model.Exercise
import com.example.fitplan.repository.PlansRepositoryFirebase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
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
    private var currentTab = "Back"
    private val pickImageViewModel : PickImageViewModel by viewModels()
    val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { pickedUri ->
            val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, pickedUri)
            bitmap?.let { pickedBitmap ->
                pickImageViewModel.setPhotoBitmap(pickedBitmap)
            }
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

        binding.savePlanBtn.isEnabled = false
        binding.savePlanBtn.alpha = 0.5f

        sharedViewModel.exerciseToPlan.observe(viewLifecycleOwner) { exercises ->
            if (exercises.isNotEmpty()) {
                binding.savePlanBtn.isEnabled = true
                binding.savePlanBtn.alpha = 1f
            }
        }



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
            var chosenImage : Bitmap? = null

            chooseImageBtn.setOnClickListener{
                pickImageLauncher.launch("image/*")

                pickImageViewModel.photoBitmap.observe(viewLifecycleOwner){bitmap ->
                    bitmap?.let { pickedBitmap ->
                        planImage.setImageBitmap(pickedBitmap)
                        chosenImage = pickedBitmap
                    }
                }
            }

            sharedViewModel.exerciseToPlan.observe(viewLifecycleOwner) { exercises ->

                saveBtn.setOnClickListener {
                    if (titleText.text.isNullOrEmpty())
                        Toast.makeText(requireContext(),
                            getString(R.string.plan_must_have_a_title),Toast.LENGTH_SHORT).show()
                    else {
                        val imageBitmap = if (chosenImage != null){
                            chosenImage
                        } else {
                            BitmapFactory.decodeResource(resources, R.drawable.default_plan_image)
                        }
                        viewModel.addPlan(
                            titleText.text.toString(),
                            descriptionText.text.toString(),
                            imageBitmap,
                            exercises
                        )
                        Toast.makeText(requireContext(),
                            getString(R.string.plan_saved), Toast.LENGTH_SHORT).show()
                        binding.savePlanBtn.isEnabled = false
                        binding.savePlanBtn.alpha = 0.5f
                        alertDialog.dismiss()
                        sharedViewModel.cleanExerciseToPlan()
                    }
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


        val exerciseData = ExercisesData(requireContext())
        val allExercises = exerciseData.exercisesByBodyPart.values.flatten()
        planExerciseAdapter = PlanExerciseAdapter(allExercises, exerciseListener, exerciseViewModel)

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = planExerciseAdapter

        binding.tabs.getTabAt(getTabIndexForBodyPart(currentTab))?.select()


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val bodyPart = getBodyPartForTabIndex(it.position)
                    when (bodyPart) {
                        "Back" -> filterExercises("Back")
                        "Chest" -> filterExercises("Chest")
                        "Abs" -> filterExercises("Abs")
                        "Legs" -> filterExercises("Legs")
                        "Cardio" -> filterExercises("Cardio")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    private fun filterExercises(bodyPart: String) {
        val exercisesData = ExercisesData(requireContext())
        val exercisesForBodyPart = exercisesData.exercisesByBodyPart[bodyPart] ?: emptyList()
        (binding.recycler.adapter as? PlanExerciseAdapter)?.setExercises(exercisesForBodyPart)
    }

    private val exerciseListener = object : PlanExerciseAdapter.ExerciseListener {
        override fun onExerciseClicked(index: Int) {
            val exercise = planExerciseAdapter.exerciseAt(index)
            currentTab = exercise.bodyPart
            sharedViewModel.setSelectedExercise(exercise)
            findNavController().navigate(R.id.action_planWorkoutFragment2_to_planExerciseCardFragment)

        }

        override fun onExerciseLongClicked(index: Int) {
            TODO("Not yet implemented")
        }

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

    override fun onResume() {
        super.onResume()
        when (currentTab) {
            "Back" -> filterExercises("Back")
            "Chest" -> filterExercises("Chest")
            "Abs" -> filterExercises("Abs")
            "Legs" -> filterExercises("Legs")
            "Cardio" -> filterExercises("Cardio")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

