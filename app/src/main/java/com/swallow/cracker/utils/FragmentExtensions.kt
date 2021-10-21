package com.swallow.cracker.utils

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.ui.model.Message

fun Fragment.toast(@StringRes stringRes: Int) {
    Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
}

fun Fragment.showMessage(msg: Message<*>) {
    when (msg.message) {
        is Int -> Toast.makeText(context, getString(msg.message), Toast.LENGTH_SHORT).show()
        is String -> Toast.makeText(context, msg.message, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.getNoInternetConnectionSnackBar(view: View): Snackbar {
    val tint = ContextCompat.getColor(requireContext(), R.color.red)
    val actionTextColor = ContextCompat.getColor(requireContext(), R.color.white)

    return Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
        .setAction("Hide") {}
        .setBackgroundTint(tint)
        .setActionTextColor(actionTextColor)
}

fun Fragment.getDataFormCacheSnackBar(view: View): Snackbar {
    val tint = ContextCompat.getColor(requireContext(), R.color.gray)

    return Snackbar.make(view, R.string.data_from_the_cache, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(tint)
}
