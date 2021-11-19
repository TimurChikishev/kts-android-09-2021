package com.swallow.cracker.utils

import org.ocpsoft.prettytime.PrettyTime
import java.util.*

fun Long.convertLongToTime(): String {
    return PrettyTime().format(Date(this * 1000))
}
