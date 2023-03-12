package com.example.steplink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class weght : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weght)
    }

    fun to_main(view: View){
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}