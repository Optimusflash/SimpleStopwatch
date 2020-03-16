package com.optimus.simplestopwatch.extensions

import androidx.lifecycle.MutableLiveData

/**
 * Created by Dmitriy Chebotar on 16.03.2020.
 */

fun <T : Any?> MutableLiveData<T>.default(defaultValue: T?): MutableLiveData<T> = apply {
    value = defaultValue
}

fun <T> MutableLiveData<T>.set(newValue: T) = apply { value = newValue }