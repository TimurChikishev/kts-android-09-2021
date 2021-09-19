package com.example.lecture1.ui.viewmodels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class LoginState {
    @Parcelize
    object DefaultState : LoginState(), Parcelable

    @Parcelize
    object LoginSuccessfulState : LoginState(), Parcelable

    @Parcelize
    object LoginButtonEnable : LoginState(), Parcelable

    @Parcelize
    class ErrorState<T>(val message: @RawValue T) : LoginState(), Parcelable
}