package com.example.taskmananger.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent : Intent = Intent(this@SplashScreenActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
