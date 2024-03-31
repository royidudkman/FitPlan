package com.example.fitplan.UI.fragments.social

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
import com.example.fitplan.view_models.SharedViewModel
import com.example.fitplan.adapters.SocialAdapter
import com.example.fitplan.databinding.FragmentSocialBinding
import com.example.fitplan.repository.PlansRepositoryFirebase
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase
import il.co.syntax.myapplication.util.Resource


class SocialFragment : Fragment() {
    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!

    private lateinit var socialAdapter: SocialAdapter
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private val viewModel : SocialViewModel by viewModels{
        SocialViewModel.SocialViewModelFactory(
            AuthRepositoryFirebase(),
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
            findNavController().navigate(R.id.action_socialFragment_to_mySocialWorkoutFragment2)

        }

        override fun onPlanLongClicked(index: Int) {
            val item = socialAdapter.planAt(index)

            sharedViewModel.sharedPlans.value?.let { plans ->
                if(plans.any{it.id == item.id}){
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.this_action_will_delete_the_plan_from_the_social))
                        .setMessage(getString(R.string.are_you_sure_you_want_to_delete_the_plan))
                        .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                            socialAdapter.deletePlanAt(index)
                            viewModel.deleteSocialPlan(item.id)
                            Toast.makeText(requireContext(), getString(R.string.plan_deleted), Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton(getString(R.string.no)) { dialog, which ->
                            dialog.dismiss()
                        }.show()
                } else{
                    Toast.makeText(requireContext(),
                        getString(R.string.you_can_t_delete_other_people_s_plans), Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}