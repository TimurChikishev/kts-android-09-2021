package com.swallow.cracker.utils

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.set(newValue: T) = apply { value = newValue }