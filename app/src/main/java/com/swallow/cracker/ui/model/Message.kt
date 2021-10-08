package com.swallow.cracker.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class Message<T>(val message: @RawValue T) : Parcelable