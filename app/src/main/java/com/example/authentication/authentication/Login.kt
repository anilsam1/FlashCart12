package com.example.authentication.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
 import com.example.authentication.R
import com.example.authentication.databinding.FragmentLoginBinding
import com.example.authentication.home.HomeActivity
import com.example.authentication.home.HomeFragement

import com.example.authentication.home.Oos
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.LoginUser
import com.example.authentication.retrofit.model.RegistrationResponce
import retrofit2.Call
import retrofit2.Response

class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        if (Oos.sharedPreferences!!.contains("EMAIL") &&
            Oos.sharedPreferences!!.contains("JWTTOKEN")&&
            Oos.sharedPreferences!!.contains("Password")
        ) {

            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)


        }
        binding.overlay.visibility = View.GONE
        binding.materialCardView.visibility = View.VISIBLE

        _binding!!.buttonL.setOnClickListener {

            binding.buttonL.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            var email = _binding!!.username.text.toString()
            var password = _binding!!.password.text.toString()



            if (_binding!!.username.text.toString().isEmpty()) {
                binding.UsernameWarning.visibility=View.VISIBLE
                binding.buttonL.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.username.setOnClickListener{
                    binding.UsernameWarning.visibility=View.GONE
                }
            } else if (password.isEmpty()) {
                binding.PasswordWarning.visibility=View.VISIBLE
                binding.buttonL.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.password.setOnClickListener{
                  binding.PasswordWarning.visibility=View.GONE
                }

            }   else {
                binding.UsernameWarning.visibility=View.GONE
                binding.PasswordWarning.visibility=View.GONE
            }

            login(email , password)


        }

        _binding!!.signup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_registration)
        }
        _binding!!.forgot.setOnClickListener {
            Log.e("Error" , "login")
            findNavController().navigate(R.id.action_login_to_forgot2) }

        return binding.root
    }




    fun  login(email1 :String , password1 :String){
        val login = LoginUser(email1, password1)
        val call = AuthApi.retrofitService.loginUser(login)
        call.enqueue(object : retrofit2.Callback<RegistrationResponce> {
            override fun onResponse(
                call: Call<RegistrationResponce>,
                response: Response<RegistrationResponce>
            ) {
                if (response.code() == 200) {
                    binding.buttonL.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE

                    Oos.editor?.apply {
                        Log.e("flow" ,"hear1")
                        response.body()?.data?.let {
                            putString("PHONENO", it.mobileNo).toString()
                            putString("EMAIL", it.emailId).toString()
                            putString("UID", it._id).toString()
                            putString("Password", password1).toString()
                            putString("JWTTOKEN", it.jwtToken).toString()
                            putString("NAME", it.name).toString()
                            putBoolean("On" , true).toString()

                        }
                    }?.commit()
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    binding.username.text?.clear()
                    binding.password.text?.clear()
                    startActivity(intent)


                } else if (response.code() == 400) {
                    binding.buttonL.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegistrationResponce>, t: Throwable) {
                binding.buttonL.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Something wrong", Toast.LENGTH_LONG).show()
            }

        })


    }




}
