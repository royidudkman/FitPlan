package com.example.fitplan.UI.run

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fitplan.R
import com.example.fitplan.UI.fragments.MenuManager
import com.example.fitplan.databinding.FragmentRunBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class RunFragment : Fragment() {
    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RunViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunBinding.inflate(inflater, container, false)
        

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isRunningHappend = false


        val bottomMenu =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomMenu.visibility = View.VISIBLE


        viewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            if (location != null) {
                viewModel.kmForMinute()
                viewModel.updateDistance(location)
            }
            binding.kmTv.text = viewModel.getTotalDistance().toString()
        }

        viewModel.getTime().observe(viewLifecycleOwner) { time ->
            viewModel.kmForMinute()
            binding.timeTv.text = time

        }

        viewModel.getKmForMinute().observe(viewLifecycleOwner) { kmForMinute ->
            binding.kmMTv.text = kmForMinute
        }
        binding.stopPauseBtn.isEnabled = false
        binding.stopPauseBtn.alpha = 0.5f

        binding.startBtn.setOnClickListener {
            MenuManager.menuInRunNotEnable(bottomMenu,ShowCantMoveToOtherMenuItem())
            viewModel.checkLocationPermissions(requireActivity(),
                object : RunViewModel.ICheckLocationPermissionListener {
                    override fun onPermissionGranted() {
                        when (viewModel.isRunning()) {
                            false -> {
                                if (!isRunningHappend) {
                                    viewModel.onStartRunning(requireActivity())
                                    binding.startBtn.icon =
                                        resources.getDrawable(R.drawable.pause_svgrepo_com_full)
                                    binding.startBtn.icon.setTint(resources.getColor(R.color.black))
                                } else {
                                    viewModel.onResumeRunning(requireActivity())
                                    binding.startBtn.icon =
                                        resources.getDrawable(R.drawable.pause_svgrepo_com_full)
                                }

                            }

                            true -> {
                                viewModel.onPauseRunning()
                                binding.startBtn.icon =
                                    resources.getDrawable(R.drawable.play_svgrepo_com_full)
                            }
                        }
                        binding.stopPauseBtn.isEnabled = true
                        binding.stopPauseBtn.alpha = 1f

                        binding.stopPauseBtn.setOnClickListener {
                            binding.startBtn.icon =
                                resources.getDrawable(R.drawable.play_svgrepo_com_full)
                            binding.startBtn.icon.setTint(resources.getColor(R.color.black))
                            findNavController().navigate(R.id.runSaveDetails)
                        }
                    }

                    override fun onPermissionDenied() {
                        MenuManager.menuEnable(findNavController(),bottomMenu,requireActivity().supportFragmentManager)
                        showPermissionRationaleDialog()
                    }

                    override fun onLocationOrNetworkDisable() {
                        MenuManager.menuEnable(findNavController(),bottomMenu,requireActivity().supportFragmentManager)
                        viewModel.onStopRunning()
                        binding.startBtn.icon = resources.getDrawable(R.drawable.play_svgrepo_com_full)
                        binding.stopPauseBtn.isEnabled = false
                        binding.stopPauseBtn.alpha = 0.5f
                        showOpenLocationServiceDialog()
                    }
                })
        }
    }

    private  fun ShowCantMoveToOtherMenuItem() : AlertDialog.Builder? {
       return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.finish_run))
            .setMessage(getString(R.string.please_finish_your_run_first))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                dialog.dismiss()
            }
    }


    private fun showOpenLocationServiceDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.location_service_required))
            .setMessage(getString(R.string.location_service_is_required_to_continue_please_open_location_service_in_setting))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.location_permission_is_required_to_continue))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                viewModel.requestPermissions(requireActivity())
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            }
            .show()

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}