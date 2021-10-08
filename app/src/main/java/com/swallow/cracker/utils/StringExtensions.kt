package com.swallow.cracker.utils

import android.util.Patterns

fun String.isUrl() = Patterns.WEB_URL.matcher(this).matches()