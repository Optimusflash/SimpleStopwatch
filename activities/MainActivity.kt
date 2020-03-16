package com.optimus.simplestopwatch.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.optimus.simplestopwatch.R
import com.optimus.simplestopwatch.helpers.StopwatchState
import com.optimus.simplestopwatch.services.StopwatchService
import com.optimus.simplestopwatch.services.StopwatchService.Companion.ACTION_PAUSE
import com.optimus.simplestopwatch.services.StopwatchService.Companion.ACTION_RESET
import com.optimus.simplestopwatch.services.StopwatchService.Companion.ACTION_START
import com.optimus.simplestopwatch.services.StopwatchService.Companion.BROADCAST_ACTION_PAUSE
import com.optimus.simplestopwatch.services.StopwatchService.Companion.BROADCAST_ACTION_RESET
import com.optimus.simplestopwatch.services.StopwatchService.Companion.BROADCAST_ACTION_START
import com.optimus.simplestopwatch.services.StopwatchService.Companion.EXTRA_TIME
import com.optimus.simplestopwatch.viewmodels.StopwatchViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var stopwatchViewModel: StopwatchViewModel
    private lateinit var currentState: StopwatchState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initViewModel()
        initReceiver()

    }

    private fun initReceiver() {

        val broadcastReceiver = object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    when(it.action){
                        BROADCAST_ACTION_START -> {
                            val currentTime = it.getLongExtra(EXTRA_TIME, 0)
                            stopwatchViewModel.updateTime(currentTime, StopwatchState.PROGRESS)
                        }
                        BROADCAST_ACTION_PAUSE -> {
                            val pausedTime = it.getLongExtra(EXTRA_TIME, 0)
                            stopwatchViewModel.updateTime(pausedTime,StopwatchState.PAUSED)
                        }
                        BROADCAST_ACTION_RESET -> {
                            stopwatchViewModel.updateTime(0,StopwatchState.DEFAULT)
                        }
                    }
                }
            }
        }

        val filter = IntentFilter()
        filter.addAction(BROADCAST_ACTION_START)
        filter.addAction(BROADCAST_ACTION_RESET)
        filter.addAction(BROADCAST_ACTION_PAUSE)
        registerReceiver(broadcastReceiver, filter)
    }

    private fun initViews() {
        btn_start_pause.setOnClickListener {
            if (currentState != StopwatchState.PROGRESS) {
                startCountdown()
            } else {
                pauseCountDown()
            }
        }
        btn_reset.setOnClickListener {
            resetCountdown()
        }

    }

    private fun startCountdown() {
        val intent = Intent(this, StopwatchService::class.java)
        intent.action  = ACTION_START
        startService(intent)
    }

    private fun pauseCountDown() {
        val intent = Intent(this, StopwatchService::class.java)
        intent.action  = ACTION_PAUSE
        startService(intent)
    }

    private fun resetCountdown() {
        val intent = Intent(this, StopwatchService::class.java)
        intent.action  = ACTION_RESET
        stopService(intent)
    }

    private fun initViewModel() {
        stopwatchViewModel = ViewModelProviders.of(this).get(StopwatchViewModel::class.java)

        stopwatchViewModel.getCurrentState().observe(this, Observer {
            currentState = it
            updateButtons()
        })

        stopwatchViewModel.getCurrentTime().observe(this, Observer {
            tv_time.text = it
        })

    }


    private fun updateButtons() {
        val placeholder = resources.getString(R.string.time_placeholder)
        val textStartPause = if (currentState == StopwatchState.PROGRESS)
            resources.getString(R.string.pause) else resources.getString(R.string.start)

        when (currentState) {
            StopwatchState.PROGRESS -> {
                btn_start_pause.text = textStartPause
                btn_reset.visibility = View.VISIBLE
            }
            StopwatchState.PAUSED -> {
                btn_start_pause.text = textStartPause
            }
            StopwatchState.DEFAULT -> {
                tv_time.text = placeholder
                btn_start_pause.text = textStartPause
                btn_reset.visibility = View.GONE
            }
        }
    }
}
