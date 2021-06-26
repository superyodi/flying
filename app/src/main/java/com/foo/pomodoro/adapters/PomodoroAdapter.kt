package com.foo.pomodoro.adapters



import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foo.pomodoro.R
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.databinding.ListItemPomodoroBinding
import com.foo.pomodoro.ui.PomodoroListFragmentDirections
import com.foo.pomodoro.viewmodels.PomodoroViewModel
import timber.log.Timber

class PomodoroAdapter(var isTimerRunning: Boolean, var runningPomodoroId: Int) :
    ListAdapter<Pomodoro, PomodoroAdapter.ViewHolder>(

        PomodoroDiffCallback()
    ) {


    private val TAG = "PomodoroAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_pomodoro,
                parent,
                false
            ),
            isTimerRunning,
            runningPomodoroId
        )
    }

    override fun onBindViewHolder(holder: PomodoroAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    inner class ViewHolder(
        private  val binding: ListItemPomodoroBinding,
        private val isTimerRunning: Boolean,
        private val runningPomodoroId: Int
    ) : RecyclerView.ViewHolder(binding.root) {


        init {
            if(isTimerRunning) setInvalidProgressbar() else setValidProgressbar()

            binding.setClickListener { view ->
                when(view) {

                    binding.progressBar ->
                        binding.viewModel?.let {
                            navigateToTimer(it.pomoId, view)
                        }

                    else ->
                        binding.viewModel?.let {
                            navigateToPomoDetail(it.pomoId, view)
                        }

                }
            }

        }
        private fun navigateToTimer(
            pomoId: Int,
            view: View
        ) {
            val direction =
                PomodoroListFragmentDirections.actionPomodoroListFragmentToTimerFragment(pomoId)

            view.findNavController().navigate(direction)
        }

        private fun navigateToPomoDetail(
            pomoId: Int,
            view: View
        ) {
            val direction =
                PomodoroListFragmentDirections.actionPomodoroListFragmentToNewPomodoroFragment(pomoId)

            view.findNavController().navigate(direction)
        }




        private fun setInvalidProgressbar() {
            binding.invalidProgressBar.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.textViewProgress.setTextColor(Color.parseColor("#514b4b"))
        }

        @SuppressLint("ResourceAsColor")
        private fun setValidProgressbar() {
            binding.invalidProgressBar.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            binding.textViewProgress.setTextColor(Color.parseColor("#ff8e71"))

        }

        fun bind(pomodoro: Pomodoro) {
            with(binding) {
                viewModel = PomodoroViewModel(pomodoro)
                executePendingBindings()
            }
            if (pomodoro.id == runningPomodoroId) setValidProgressbar()
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