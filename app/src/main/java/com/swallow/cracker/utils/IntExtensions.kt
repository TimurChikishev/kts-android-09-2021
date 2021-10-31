package com.swallow.cracker.utils

fun Int.getBooleanVote() = when (this) {
    1 -> true
    -1 -> false
    else -> null
}