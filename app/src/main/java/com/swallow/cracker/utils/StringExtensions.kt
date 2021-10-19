package com.swallow.cracker.utils

fun String.fixImgUrl(): String {
    return this.replace("amp;s", "s").replace("amp;", "")
}