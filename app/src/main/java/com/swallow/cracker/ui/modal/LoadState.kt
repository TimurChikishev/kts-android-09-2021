package com.swallow.cracker.ui.modal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class LoadState {

    @Parcelize
    object OnDefault : LoadState(), Parcelable

    @Parcelize
    object OnSuccess : LoadState(), Parcelable

    @Parcelize
    class OnError<T>(val message: @RawValue T) : LoadState(), Parcelable

}