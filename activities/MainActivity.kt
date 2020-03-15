package com.optimus.simplestopwatch.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.optimus.simplestopwatch.R
import com.optimus.simplestopwatch.model.SimpleStopwatch
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isStarted = false
        val date = Date()
        val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.ROOT)

        val stopwatch = object : SimpleStopwatch(){
            override fun onTick(time: Long) {
                date.time = time
                val currentTime = dateFormat.format(date)
                text_view.text = currentTime
            }
        }

        button.setOnClickListener {
            if (!isStarted){
                button.text = "Pause"
                stopwatch.start()
            } else {
                button.text = "Start"
                stopwatch.pause()
            }
            isStarted = !isStarted
        }

        button2.setOnClickListener {
            text_view.text = "00:00:00.000"
            stopwatch.reset()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
