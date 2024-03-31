package com.example.fitplan.UI.fragments.my_plans

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
import com.example.fitplan.view_models.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.view_models.SharedViewModel
import com.example.fitplan.adapters.MyPlansAdapter
import com.example.fitplan.databinding.FragmentMyPlansBinding
import com.example.fitplan.repository.PlansRepositoryFirebase
import com.google.android.material.bottomnavigation.BottomNavigationView
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase
import il.co.syntax.myapplication.util.Resource

class MyPlansFragment : Fragment() {
    private var _binding: FragmentMyPlansBinding? = null
    private val binding get() = _binding!!

    private val exerciseViewModel: ExercisesViewModel by activityViewModels()

    private lateinit var myPlansAdapter: MyPlansAdapter
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private val viewModel : MyPlansViewModel by viewModels{
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
        _binding = FragmentMyPlansBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPlansAdapter = MyPlansAdapter(emptyList(), planListener, viewModel)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myPlansAdapter
        }

        viewModel.planStatus.observe(viewLifecycleOwner){ resource ->
            when(resource){
                is Resource.Loading -> {
                    binding.loadingPlansProgress.isVisible = true

                }
                is Resource.Success -> {
                    binding.loadingPlansProgress.isVisible = false
                    resource.data?.let{ plans ->
                        myPlansAdapter.updatePlans(plans)
                        sharedViewModel.setSharedPlans(plans)
                    }
                }
                is Resource.Error -> {
                    binding.loadingPlansProgress.isVisible = false
                    Toast.makeText(requireContext(), "${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private val planListener = object : MyPlansAdapter.ExerciseListener {
        override fun onPlanClicked(index: Int) {
            val clickedPlan = myPlansAdapter.planAt(index)
            sharedViewModel.setSelectedPlan(clickedPlan)
            findNavController().navigate(R.id.action_myPlansFragment_to_myWorkoutFragment)

        }

        override fun onPlanLongClicked(index: Int) {
            val item = myPlansAdapter.planAt(index)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.this_action_will_delete_the_plan))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_the_plan))
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    myPlansAdapter.deletePlanAt(index)
                    viewModel.deletePlan(item.id)
                    Toast.makeText(requireContext(),
                        getString(R.string.plan_deleted), Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPlans()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
