package com.example.fitplan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.model.Exercise
import com.example.fitplan.databinding.PlanExerciseLayoutBinding

class PlanExerciseAdapter(private val exercises: List<Exercise>, private val callback: ExerciseListener, private val viewModel: ExercisesViewModel) :
    RecyclerView.Adapter<PlanExerciseAdapter.ExerciseViewHolder>() {

    interface ExerciseListener {
        fun onExerciseClicked(index: Int)
        fun onExerciseLongClicked(index: Int)
    }

    inner class ExerciseViewHolder(private val binding: PlanExerciseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
            binding.addExerciseBtn.setOnClickListener {
                val exercise = exercises[adapterPosition]
                viewModel.addExercise(exercise)
            }
        }

        fun bind(exercise: Exercise) {
            binding.exerciseTitle.text = exercise.name
            //binding.exerciseDescription.text = exercise.description
            // Load image using Glide (example)
            Glide.with(binding.root).load(exercise.image).circleCrop().into(binding.exerciseImage)
        }

        override fun onClick(v: View?) {
            callback.onExerciseClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            callback.onExerciseLongClicked(adapterPosition)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlanExerciseLayoutBinding.inflate(inflater, parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}
