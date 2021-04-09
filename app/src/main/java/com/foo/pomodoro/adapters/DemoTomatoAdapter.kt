package com.foo.pomodoro.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foo.pomodoro.R
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.databinding.ListItemDemoBinding
import com.foo.pomodoro.viewmodels.DemoTomatoViewModel

class DemoTomatoAdapter :
    ListAdapter<Pomodoro, DemoTomatoAdapter.ViewHolder>(
        DemoTomatoDiffCallback()
    ) {

    private val TAG = "DemoTomatoAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_demo,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: DemoTomatoAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private  val binding: ListItemDemoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->

                Log.d(TAG, "demo item check")

            }
        }

        fun bind(pomodoro: Pomodoro) {
            with(binding) {
                viewModel = DemoTomatoViewModel(pomodoro)
                executePendingBindings()
            }
        }
    }
}

private class DemoTomatoDiffCallback : DiffUtil.ItemCallback<Pomodoro>() {

    override fun areItemsTheSame(
        oldItem: Pomodoro,
        newItem: Pomodoro
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Pomodoro,
        newItem: Pomodoro
    ): Boolean {
        return oldItem == newItem
    }
}
