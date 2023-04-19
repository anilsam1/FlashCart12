package com.example.authentication.authentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.authentication.R
import com.example.authentication.databinding.FragmentOtpBinding
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.RegistrationOtp
import com.example.authentication.retrofit.model.RegistrationResponce
import com.example.authentication.retrofit.model.Resendotp
import com.example.authentication.retrofit.model.ResendotpResponce
import com.otpview.OTPListener
import retrofit2.Call
import retrofit2.Response

class Otp : Fragment() {


    private var _binding: FragmentOtpBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentOtpBinding.inflate(inflater, container, false)

        val args = this.arguments
        val registration = args?.getBoolean("Registration").toString()
        val UID = args?.getString("UID").toString()
        val Email = args?.getString("Email").toString()
        val UID2 = args?.getString("ForgotPassword").toString()
        Log.e("uid2", UID2)
        binding.verificationcodeEmail.setText(Email)
        _binding!!.resend.setOnClickListener {



        }


        binding.resend.setOnClickListener {
            if (registration == "true") {
                resend(UID)
            } else {
               resend(UID2)

            }
        }
        _binding!!.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {

                if (registration == "true") {
                    Register(otp, UID)
                } else {
                    forgot(otp, UID2)
                    Log.e("ERROR ", UID2)

                }

            }
        }




        return binding.root
    }


    fun Register(otp: String, UID: String) {

        val regiOtp = RegistrationOtp(otp, UID)
        val call = AuthApi.retrofitService.verifyOtp(regiOtp)
        _binding!!.progressBar.visibility = View.VISIBLE

        call.enqueue(object : retrofit2.Callback<RegistrationResponce> {
            override fun onResponse(
                call: Call<RegistrationResponce>,
                response: Response<RegistrationResponce>
            ) {

                if (response.code() == 200) {
                    _binding!!.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_otp_to_login)


                } else if (response.code() == 400) {
                    _binding!!.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Invalid Otp", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<RegistrationResponce>, t: Throwable) {
                Toast.makeText(context, "Something wrong", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun forgot(otp: String, UID2: String) {

        val regiOtp = RegistrationOtp(otp, UID2)
        val call = AuthApi.retrofitService.verifyOtpOnForgot(regiOtp)
        _binding!!.progressBar.visibility = View.VISIBLE

        call.enqueue(object : retrofit2.Callback<RegistrationResponce> {
            override fun onResponse(
                call: Call<RegistrationResponce>,
                response: Response<RegistrationResponce>
            ) {

                if (response.code() == 200) {


                    _binding!!.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_otp_to_login)


                } else if (response.code() == 400) {
                    _binding!!.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Invalid Otp", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<RegistrationResponce>, t: Throwable) {
                Toast.makeText(context, "Something wrong", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun resend(userid : String){


            val re = Resendotp(userid)
            val call = AuthApi.retrofitService.resendotp(re)


            call.enqueue(object : retrofit2.Callback<ResendotpResponce> {
                override fun onResponse(
                    call: Call<ResendotpResponce>,
                    response: Response<ResendotpResponce>
                ) {

                    if (response.code() == 200) {
                        binding.resend.setText("Already sended")
                        binding.resend.isClickable = false

                    } else if (response.code() == 400) {

                    }

                }

                override fun onFailure(call: Call<ResendotpResponce>, t: Throwable) {
                    Toast.makeText(context, "Something wrong", Toast.LENGTH_LONG).show()
                }

            })



    }

}

