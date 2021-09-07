package com.yodi.flying.adapters



import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yodi.flying.R
import com.yodi.flying.databinding.ListItemTicketBinding
import com.yodi.flying.features.tickets.TicketViewModel
import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.model.entity.TicketWithTasks


class TicketAdapter() :
    ListAdapter<TicketWithTasks, TicketAdapter.ViewHolder>(
        TicketDiffCallback()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_ticket,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: TicketAdapter.ViewHolder, position: Int) {


        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: ListItemTicketBinding,

    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(ticketWithTasks: TicketWithTasks) {
            with(binding) {

                viewModel = TicketViewModel(ticketWithTasks)

                val taskAdapter = TaskAdapter()
                binding.taskList.adapter = taskAdapter
                taskAdapter.submitList(ticketWithTasks.tasks)
                executePendingBindings()
            }

        }
    }

}


private class TicketDiffCallback : DiffUtil.ItemCallback<TicketWithTasks>() {


    override fun areItemsTheSame(oldItem: TicketWithTasks, newItem: TicketWithTasks): Boolean {
        return oldItem.depth == newItem.depth
    }

    override fun areContentsTheSame(oldItem: TicketWithTasks, newItem: TicketWithTasks): Boolean {
        return oldItem == newItem
    }
}

