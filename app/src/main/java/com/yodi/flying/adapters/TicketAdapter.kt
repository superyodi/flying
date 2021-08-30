package com.yodi.flying.adapters



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


class TicketAdapter() :
    ListAdapter<Ticket, TicketAdapter.ViewHolder>(
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

        fun bind(ticket: Ticket) {
            with(binding) {
                viewModel = TicketViewModel(ticket)
                executePendingBindings()
            }

        }
    }



}


private class TicketDiffCallback : DiffUtil.ItemCallback<Ticket>() {


    override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
        return oldItem.depth == newItem.depth
    }

    override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
        return oldItem == newItem
    }
}

