package com.example.myapplication

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.Display
import androidx.core.view.ViewCompat.getDisplay
import android.content.Context.DISPLAY_SERVICE
import android.graphics.Rect
import androidx.core.content.ContextCompat.getSystemService
import android.hardware.display.DisplayManager
import android.net.Uri
import android.util.Log
import android.os.Build

import com.example.myapplication.Util



const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()
        Log.i("aa", message)
        // val intent = packageManager.getLaunchIntentForPackage(message)
        intent = Intent(Intent.ACTION_VIEW,
            Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=1829097786&version=1"))

        if (intent == null) return
        Log.i("aa", intent.toString())

        Util().launchApp(this, intent, view)
        // startActivity(intent, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}
