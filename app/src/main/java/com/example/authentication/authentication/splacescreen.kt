package com.example.authentication.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.authentication.MainActivity
import com.example.authentication.R

class splacescreen : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splacescreen)

        Handler().postDelayed({

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


            finish()
        }, SPLASH_DELAY)
    }
}