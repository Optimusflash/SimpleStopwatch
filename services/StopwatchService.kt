package com.optimus.simplestopwatch.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.optimus.simplestopwatch.model.SimpleStopwatch

/**
 * Created by Dmitriy Chebotar on 16.03.2020.
 */
class StopwatchService : Service() {

    companion object {
        const val ACTION_START = "com.optimus.simplestopwatch.action_start"
        const val ACTION_PAUSE = "com.optimus.simplestopwatch.action_pause"
        const val ACTION_RESET = "com.optimus.simplestopwatch.action_reset"

        const val BROADCAST_ACTION_START = "com.optimus.simplestopwatch.broadcast_action_start"
        const val BROADCAST_ACTION_PAUSE = "com.optimus.simplestopwatch.broadcast_action_pause"
        const val BROADCAST_ACTION_RESET = "com.optimus.simplestopwatch.broadcast_action_reset"

        const val EXTRA_TIME = "EXTRA_TIME"
    }

    lateinit var stopwatch : SimpleStopwatch
    private var currentTime = 0L
    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START -> {
                    val intentBroadcast = Intent(BROADCAST_ACTION_START)
                    stopwatch = SimpleStopwatch{time ->
                        currentTime = time
                        intentBroadcast.putExtra(EXTRA_TIME, time)
                        sendBroadcast(intentBroadcast)
                    }.start()
                }
                ACTION_PAUSE -> {
                    stopwatch.pause()
                    val intentBroadcast = Intent(BROADCAST_ACTION_PAUSE)
                    intentBroadcast.putExtra(EXTRA_TIME, currentTime)
                    sendBroadcast(intentBroadcast)
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        stopwatch.reset()
        val intentBroadcast = Intent(BROADCAST_ACTION_RESET)
        sendBroadcast(intentBroadcast)
        super.onDestroy()
        //TODO reset stopwatch, send broadcast "stop"
    }
}