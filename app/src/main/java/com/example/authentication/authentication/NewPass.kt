package com.example.authentication.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.authentication.MainActivity
import com.example.authentication.databinding.ActivityNewPassBinding
import com.example.authentication.home.Oos
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.NewpassResponce
import com.example.authentication.retrofit.model.newpass
import retrofit2.Call
import retrofit2.Response

lateinit var binding: ActivityNewPassBinding

class NewPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPassBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val password = binding.Password.text
        val confirmpassword = binding.confirmpassword.text


        binding.ResetPassword.setOnClickListener {

            val token =
                Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
            val update = newpass(password.toString(), confirmpassword.toString())
            val call = AuthApi.retrofitService.newpassword(update, token)
            binding!!.progressBar.visibility = View.VISIBLE

            call.enqueue(object : retrofit2.Callback<NewpassResponce> {
                override fun onResponse(
                    call: Call<NewpassResponce>,
                    response: Response<NewpassResponce>
                ) {

                    if (response.code() == 200) {
                        Oos.editor?.clear()?.commit()
                        Toast.makeText(this@NewPass, "succesfully ", Toast.LENGTH_LONG).show()

                        Handler().postDelayed({
                            val intent = Intent(this@NewPass, MainActivity::class.java)
                            startActivity(intent)
                        }, 2000)


                    } else if (response.code() == 400) {
                        Log.e("400", "not set")
                    }

                }

                override fun onFailure(call: Call<NewpassResponce>, t: Throwable) {
                    Toast.makeText(this@NewPass, "not changed", Toast.LENGTH_LONG).show()
                }

            })


        }


    }
}