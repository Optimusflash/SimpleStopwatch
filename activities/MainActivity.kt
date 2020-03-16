package com.optimus.simplestopwatch.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.optimus.simplestopwatch.R
import com.optimus.simplestopwatch.model.SimpleStopwatch
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

        private lateinit var stopwatch: SimpleStopwatch

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isStarted = false
        val date = Date()
        val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.ROOT)
            dateFormat.timeZone = TimeZone.getTimeZone("Europe")

        stopwatch = SimpleStopwatch{ time ->
                date.time = time
                val currentTime = dateFormat.format(date)
                text_view.text = currentTime
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
            isStarted = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopwatch.reset()
    }
}
