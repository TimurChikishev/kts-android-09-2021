package com.swallow.cracker.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.swallow.cracker.ui.model.Message

fun Fragment.toast(@StringRes stringRes: Int) {
    Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
}

fun Fragment.showMessage(msg: Message<*>) {
    when (msg.message) {
        is Int -> Toast.makeText(context, getString(msg.message),Toast.LENGTH_SHORT).show()
        is String -> Toast.makeText(context, msg.message, Toast.LENGTH_SHORT).show()
    }
}
