package com.swallow.cracker.data.model

sealed class Resources<out T> {
    class Error<T>(val throwable: Throwable? = null) : Resources<T>()
    data class Success<T>(val value: T) : Resources<T>()
}
