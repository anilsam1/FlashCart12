package com.example.authentication.home

import android.app.Application
import android.content.SharedPreferences

class Oos:Application() {


    companion object{

        var sharedPreferences: SharedPreferences? = null
        var editor:SharedPreferences.Editor? = null

    }


    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("sharedprefrence", MODE_PRIVATE)
        editor = sharedPreferences!!.edit()



    }

}



