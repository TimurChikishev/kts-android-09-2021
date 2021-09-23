package com.swallow.cracker.utils

import androidx.lifecycle.MutableLiveData

fun <T : Any?> MutableLiveData<T>.default(initializeValue: T) = apply { value = initializeValue }

fun <T> MutableLiveData<T>.set(newValue: T) = apply { value = newValue }