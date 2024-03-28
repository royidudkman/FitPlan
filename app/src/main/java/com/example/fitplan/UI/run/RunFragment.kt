package com.example.fitplan.UI.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fitplan.R
import com.example.fitplan.databinding.FragmentRunBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


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

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE

        // observe of distance
        viewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            if (location != null) {
                viewModel.kmForMinute()
                viewModel.updateDistance(location)
            }
            binding.kmTv.text = viewModel.getTotalDistance().toString()
        }

        //observe of time
        viewModel.getTime().observe(viewLifecycleOwner) { time ->
            viewModel.kmForMinute()
            binding.timeTv.text = time

        }

        //observe of km for minute
        viewModel.getKmForMinute().observe(viewLifecycleOwner){kmForMinute ->
            binding.kmMTv.text = kmForMinute
        }

        binding.startBtn.setOnClickListener {
            when (viewModel.isRunning()) {
                false -> {
                    if (!isRunningHappend){
                        viewModel.onStartRunning()
                        binding.startBtn.icon = resources.getDrawable(R.drawable.pause_svgrepo_com_full)
                        binding.startBtn.icon.setTint(resources.getColor(R.color.black))
                        isRunningHappend=true
                    }
                    else{
                        viewModel.onResumeRunning()
                        binding.startBtn.icon = resources.getDrawable(R.drawable.pause_svgrepo_com_full)
                    }

                }
                true -> { // If already started, pause the run
                    viewModel.onPauseRunning()
                    binding.startBtn.icon = resources.getDrawable(R.drawable.play_svgrepo_com_full)
                }
            }
        }

        binding.stopPauseBtn.setOnClickListener {
            viewModel.onStopRunning()
            binding.startBtn.icon = resources.getDrawable(R.drawable.play_svgrepo_com_full)
            binding.startBtn.icon.setTint(resources.getColor(R.color.black))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}