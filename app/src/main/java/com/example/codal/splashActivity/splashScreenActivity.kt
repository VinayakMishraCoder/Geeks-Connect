package com.example.codal.splashActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.codal.MainActivity
import com.example.codal.R

class splashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}