package com.yodi.flying.custom

import android.app.Dialog
import android.content.Context
import com.yodi.flying.R

class WaitingDialog {
    companion object {
        fun create(context: Context): Dialog {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_waiting)
            dialog.window?.setBackgroundDrawableResource(R.color.transparent)
            return dialog
        }

    }
}