package com.example.codal.sortedLists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.codal.R

class CodeChefList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_chef_list)
        supportActionBar?.hide()
    }
}