package com.example.fitplan.UI.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.SharedViewModel
import com.example.fitplan.databinding.MyExerciseCardLayoutBinding
import com.example.serviceapp.AlarmService
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class MyExerciseCardFragment : Fragment() , ExercisesViewModel.TimerCallback{
    private var _binding: MyExerciseCardLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExercisesViewModel by activityViewModels()
    private var timer: CountDownTimer? = null

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyExerciseCardLayoutBinding.inflate(inflater, container, false)

        sharedViewModel.selectedExercise.observe(viewLifecycleOwner){ exercise ->

            Glide.with(requireContext()).asGif().load(exercise.image).into(binding.exerciseImage)
            binding.titleTv.setText(exercise.name)
            binding.descriptionTv.setText(exercise.description)
            binding.repsTv.setText("Reps: " + exercise.reps.toString())

            val totalSeconds = exercise.time / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            binding.timerTv.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

            if (exercise.time == 0L){
                binding.timerTv.visibility = View.GONE
                binding.startTimerBtn.visibility = View.GONE
                binding.startTimerBtn.isEnabled = false
            }

            binding.startTimerBtn.setOnClickListener {
                startTimer(exercise.time)
            }


        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
        viewModel.timerCallback = this
    }


    override fun onDestroyView() {
        super.onDestroyView()
        stopTimer()
        requireContext().stopService(Intent(requireContext(), AlarmService::class.java))
        _binding = null
    }

    override fun startTimer(milliseconds: Long) {
        timer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = ((millisUntilFinished / 1000) % 3600) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                binding.timerTv.setText(timeFormatted)

            }

            override fun onFinish() {
                binding.timerTv.text = "00:00"
                val serviceIntent = Intent(requireContext(), AlarmService::class.java)
                requireContext().startService(serviceIntent)
            }
        }.start()

    }
    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}