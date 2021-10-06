package com.swallow.cracker.ui.modal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class VoteState {
    @Parcelize
    object OnDefault : VoteState(), Parcelable

    @Parcelize
    data class OnError<T>(val message: @RawValue T) : VoteState(), Parcelable

    @Parcelize
    data class OnSuccess(val likes: Boolean, val position: Int? = null) : VoteState(), Parcelable
}