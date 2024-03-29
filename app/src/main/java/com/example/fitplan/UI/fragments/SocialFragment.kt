package com.example.fitplan.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplan.R
import com.example.fitplan.SharedViewModel
import com.example.fitplan.adapters.MyPlansAdapter
import com.example.fitplan.adapters.SocialAdapter
import com.example.fitplan.databinding.FragmentSocialBinding
import com.example.fitplan.repository.PlansRepositoryFirebase
import com.google.android.material.bottomnavigation.BottomNavigationView
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase
import il.co.syntax.myapplication.util.Resource


class SocialFragment : Fragment() {
    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!

    private lateinit var socialAdapter: SocialAdapter
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private val viewModel : SocialViewModel by viewModels{
        SocialViewModel.SocialViewModelFactory(AuthRepositoryFirebase(),
            PlansRepositoryFirebase()
        )
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialBinding.inflate(inflater, container, false)


        binding.uploadPlanBtn.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_choosePlanToUploadFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socialAdapter = SocialAdapter(emptyList(), socialPlanListener, viewModel)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = socialAdapter
        }

        viewModel.planStatus.observe(viewLifecycleOwner){ resource ->
            when(resource){
                is Resource.Loading -> {
                    binding.loadingPlansProgress.isVisible = true

                }
                is Resource.Success -> {
                    binding.loadingPlansProgress.isVisible = false
                    resource.data?.let{ plans ->
                        socialAdapter.updatePlans(plans)
                    }
                }
                is Resource.Error -> {
                    binding.loadingPlansProgress.isVisible = false
                    Toast.makeText(requireContext(), "${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private val socialPlanListener = object : SocialAdapter.ExerciseListener {
        override fun onPlanClicked(index: Int) {
            val clickedPlan = socialAdapter.planAt(index)
            sharedViewModel.setSelectedPlan(clickedPlan)
            findNavController().navigate(R.id.action_socialFragment_to_myWorkoutFragment)

        }

        override fun onPlanLongClicked(index: Int) {
            val item = socialAdapter.planAt(index)

            sharedViewModel.sharedPlans.value?.let { plans ->
                if(plans.any{it.id == item.id}){
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("This action will delete the plan from the Social")
                        .setMessage("Are you sure you want to delete the plan?")
                        .setPositiveButton("Yes") { dialog, which ->
                            socialAdapter.deletePlanAt(index)
                            viewModel.deleteSocialPlan(item.id)
                            Toast.makeText(requireContext(), "Plan deleted", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("No") { dialog, which ->
                            dialog.dismiss()
                        }.show()
                } else{
                    Toast.makeText(requireContext(), "You can't delete other people's plans", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}