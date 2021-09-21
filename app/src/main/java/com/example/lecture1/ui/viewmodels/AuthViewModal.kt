package com.example.lecture1.ui.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.lecture1.R
import com.example.lecture1.utils.set

class AuthViewModal(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableState = savedStateHandle.getLiveData<LoginState>("login", LoginState.DefaultState)

    val state : LiveData<LoginState>
        get() = mutableState

    // т.к. мы делаем кнопку enable только тогда, когда
    // все поля прошли валидацию, можно убрать валидацию тут,
    // но малоли кто-то решит ипользовать это подругому и что-то пойдет не так
    fun login(email: String, password: String) {
        if (!validateEmail(email = email)){
            mutableState.set(newValue = LoginState.ErrorState(message = R.string.error_email))
            return
        }

        if (!validatePassword(password = password)) {
            mutableState.set(newValue = LoginState.ErrorState(message = R.string.error_password))
            return
        }

        mutableState.set(newValue = LoginState.LoginSuccessfulState)
        // тут можно будет сделать асинхронную проверку на наличие пользователя в репозитории
        // (надо будет добавить состояние LoadingState)
    }

    fun isValidate(email: String, password: String) {
        if (!validateEmail(email = email) || !validatePassword(password = password)) {
            mutableState.set(newValue = LoginState.DefaultState)
            return
        }

        mutableState.set(newValue = LoginState.LoginButtonEnable)
    }

    private fun validateEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validatePassword(password: String) = password.length >= 8
}