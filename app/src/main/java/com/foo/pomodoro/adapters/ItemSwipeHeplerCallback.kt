package com.foo.pomodoro.adapters


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView



open class ItemSwipeHeplerCallback() : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val mFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val sFlag = ItemTouchHelper.END or ItemTouchHelper.START
        return makeMovementFlags(mFlag,sFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }


    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}