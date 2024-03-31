package com.example.fitplan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitplan.view_models.ExercisesViewModel
import com.example.fitplan.model.Exercise
import com.example.fitplan.databinding.MyExerciseLayoutBinding

class MyExerciseAdapter(private var exercises: List<Exercise>, private val callback: ExerciseListener, private val viewModel: ExercisesViewModel) :
    RecyclerView.Adapter<MyExerciseAdapter.ExerciseViewHolder>() {


    interface ExerciseListener {
        fun onExerciseClicked(index: Int)
        fun onExerciseLongClicked(index: Int)
    }

    inner class ExerciseViewHolder(private val binding: MyExerciseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        fun bind(exercise: Exercise) {
            binding.exerciseTitle.text = exercise.name
            binding.exerciseDescription.text = exercise.description
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

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
    }

    fun exerciseAt(position: Int) = exercises[position]

    fun updateExercises(newExercises: List<Exercise>) {
        exercises = newExercises

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyExerciseLayoutBinding.inflate(inflater, parent, false)
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
