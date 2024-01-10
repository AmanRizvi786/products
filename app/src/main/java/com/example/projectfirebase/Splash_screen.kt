package com.example.projectfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler=android.os.Handler()
        handler.postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
        },2000)
    }
}