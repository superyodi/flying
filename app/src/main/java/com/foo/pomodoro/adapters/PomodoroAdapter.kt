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
import com.foo.pomodoro.databinding.ListItemPomodoroBinding
import com.foo.pomodoro.viewmodels.PomodoroViewModel

class PomodoroAdapter :
    ListAdapter<Pomodoro, PomodoroAdapter.ViewHolder>(
        PomodoroDiffCallback()
    ) {

    private val TAG = "DemoTomatoAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_pomodoro,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: PomodoroAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private  val binding: ListItemPomodoroBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->

                Log.d(TAG, "demo item check")

            }
        }

        fun bind(pomodoro: Pomodoro) {
            with(binding) {
                viewModel = PomodoroViewModel(pomodoro)
                executePendingBindings()
            }
        }
    }
}

private class PomodoroDiffCallback : DiffUtil.ItemCallback<Pomodoro>() {

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
