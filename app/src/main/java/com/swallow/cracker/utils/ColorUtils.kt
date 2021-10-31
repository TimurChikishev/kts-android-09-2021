package com.swallow.cracker.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun getColor(context: Context, @ColorRes colorId: Int) = ContextCompat.getColor(context, colorId)

fun resolveColorAttr(context: Context, @AttrRes colorAttr: Int): Int {
    val resolvedAttr = resolveThemeAttr(context, colorAttr)
    // resourceId is used if it's a ColorStateList, and data if it's a color reference or a hex color
    val colorRes = if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
    return ContextCompat.getColor(context, colorRes)
}

private fun resolveThemeAttr(context: Context, @AttrRes attrRes: Int): TypedValue {
    val typedValue = TypedValue()
    context.theme?.resolveAttribute(attrRes, typedValue, true)
    return typedValue
}