package com.example.myapplication

import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

class BeepHelper
{
    val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

    fun beep(duration: Int = 750)
    {

        val handler = Handler(Looper.getMainLooper())
        val runnable = object :Runnable {
            override fun run() {
                toneG.startTone(ToneGenerator.TONE_DTMF_P, duration)
                handler.postDelayed(this, (duration+1000).toLong())
            }
        }
        handler.post(runnable)
    }

}

class DisplayMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.textView).apply {
            text = message
        }

        BeepHelper().beep()}


}
