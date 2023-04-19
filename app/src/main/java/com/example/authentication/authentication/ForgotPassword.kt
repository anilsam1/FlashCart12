package com.example.authentication.authentication

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.authentication.R
import com.example.authentication.databinding.FragmentForgotBinding
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.Forgot
import com.example.authentication.retrofit.model.RegistrationResponce
import retrofit2.Call
import retrofit2.Response

class ForgotPassword : Fragment() {

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding!!.getOtpForgot.setOnClickListener {
            binding.getOtpForgot.visibility =View.GONE
            _binding!!.progressBar.visibility = View.VISIBLE
            val emailforgot = binding.email2.text.toString().trim()
            Log.e("emailForgot",emailforgot)
            if (emailforgot.isEmpty()) {
                binding.email2.error = "Email cannot be empty"
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailforgot).matches()) {
                binding.email2.error = "Invalid email address"
                return@setOnClickListener
            }

            val forgot = Forgot(emailforgot)
            val call = AuthApi.retrofitService.forgotPassword(forgot)


            call.enqueue(object : retrofit2.Callback<RegistrationResponce> {

                override fun onResponse(
                    call: Call<RegistrationResponce>,
                    response: Response<RegistrationResponce>
                )
                {
                    if (response.code() == 200) {
                        binding.getOtpForgot.visibility =View.VISIBLE
                        _binding!!.progressBar.visibility = View.GONE
                        val userId = response.body()!!.data!!._id
                        val bundle = Bundle()
                        bundle.putString("Email", emailforgot)
                        bundle.putString("ForgotPassword", userId)
                        val fragment: Fragment = Otp()
                        fragment.arguments = bundle

                        findNavController().navigate(R.id.action_forgot2_to_otp,bundle)
                    } else if (response.code() == 400) {
                        binding.getOtpForgot.visibility =View.VISIBLE
                        _binding!!.progressBar.visibility = View.GONE
                        Toast.makeText(context, "User not registered", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<RegistrationResponce>, t: Throwable) {
                    binding.getOtpForgot.visibility =View.VISIBLE
                    _binding!!.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

}
