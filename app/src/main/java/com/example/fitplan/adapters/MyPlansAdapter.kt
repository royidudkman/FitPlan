package com.example.fitplan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitplan.UI.fragments.my_plans.MyPlansViewModel
import com.example.fitplan.databinding.MyExerciseLayoutBinding
import com.example.fitplan.model.Plan

class MyPlansAdapter(private var plans: List<Plan>, private val callback: ExerciseListener, private val viewModel: MyPlansViewModel) :
    RecyclerView.Adapter<MyPlansAdapter.PlanViewHolder>() {

    interface ExerciseListener {
        fun onPlanClicked(index: Int)
        fun onPlanLongClicked(index: Int)
    }

    inner class PlanViewHolder(private val binding: MyExerciseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        fun bind(plan: Plan) {
            binding.exerciseTitle.text = plan.title
            binding.exerciseDescription.text = plan.description
            Glide.with(binding.root).load(plan.bitmap).circleCrop().into(binding.exerciseImage)
        }

        override fun onClick(v: View?) {
            callback.onPlanClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            callback.onPlanLongClicked(adapterPosition)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyExerciseLayoutBinding.inflate(inflater, parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.bind(plan)
    }

    override fun getItemCount(): Int {
        return plans.size
    }

    fun updatePlans(newPlans: List<Plan>) {
        plans = newPlans
        notifyDataSetChanged()
    }

    fun planAt(position: Int): Plan {
        return plans[position]
    }

    fun deletePlanAt(position: Int) {
        val deletedPlan = plans[position]
        plans = plans.filterIndexed { index, _ -> index != position }
        notifyItemRemoved(position)
        viewModel.deletePlan(deletedPlan.id)
    }
}
