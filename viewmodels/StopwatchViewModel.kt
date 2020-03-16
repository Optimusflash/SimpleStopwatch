package com.optimus.simplestopwatch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.optimus.simplestopwatch.extensions.default
import com.optimus.simplestopwatch.extensions.set
import com.optimus.simplestopwatch.helpers.StopwatchState
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Dmitriy Chebotar on 16.03.2020.
 */
class StopwatchViewModel : ViewModel() {

//
//stopwatch = SimpleStopwatch{ time ->
//    date.time = time
//    val currentTime = dateFormat.format(date)
//    tv_time.text = currentTime

    private val stopwatchState = MutableLiveData<StopwatchState>().default(StopwatchState.DEFAULT)
    private val time = MutableLiveData<String>().default("0:00:00.00")

    private val date = Date()
    private lateinit var dateFormat: SimpleDateFormat

    init {
        initDate()
    }

    private fun initDate() {
        dateFormat = SimpleDateFormat("HH:mm:ss.SS", Locale.ROOT)
        dateFormat.timeZone = TimeZone.getTimeZone("Europe")
    }


    fun getCurrentState(): LiveData<StopwatchState> {
        return stopwatchState
    }

    fun getCurrentTime(): LiveData<String> {
        return time
    }

    fun updateTime(time: Long, stopwatchState: StopwatchState) {
        this.stopwatchState.set(stopwatchState)
        when (stopwatchState) {
            StopwatchState.PROGRESS -> {
                date.time = time
                val formattedTime = dateFormat.format(date)
                this.time.set(formattedTime)
            }
            StopwatchState.PAUSED -> {
                date.time = time
                val formattedTime = dateFormat.format(date)
                this.time.set(formattedTime)
            }
            StopwatchState.DEFAULT -> {
                date.time = time
                this.time.set("0:00:00.00")
            }
        }
    }
}