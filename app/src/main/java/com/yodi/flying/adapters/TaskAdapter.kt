package com.yodi.flying.adapters

import com.yodi.flying.databinding.ListItemTicketTaskBinding
import com.yodi.flying.features.tickets.TaskViewModel
import com.yodi.flying.model.entity.Task
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yodi.flying.R



class TaskAdapter() :
    ListAdapter<Task, TaskAdapter.ViewHolder>(
        TaskDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_ticket_task,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: ListItemTicketTaskBinding,

        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            with(binding) {
                viewModel = TaskViewModel(task)
                executePendingBindings()
            }

        }
    }

}


private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {


    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.pomoId == newItem.pomoId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}

