package com.swallow.cracker.utils

import android.content.Intent

fun sharedUrl(url: String): Intent = Intent.createChooser(
    Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    },
    null
)