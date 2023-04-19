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
import com.example.authentication.databinding.FragmentRegistrationBinding
import com.example.authentication.home.Oos
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.RegistrationResponce
import com.example.authentication.retrofit.model.RegistrationUser
import retrofit2.Call
import retrofit2.Response

class Registration : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        _binding!!.buttonSignUp.setOnClickListener {


            binding.buttonSignUp.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            val email = _binding?.email?.text.toString()
            val mobileNo =  _binding?.mobileNo?.text.toString()
            val fullName =  _binding?.fullname?.text.toString()
            val password = _binding?.password?.text.toString()


            if (_binding!!.email.text.toString().isEmpty()) {
                _binding!!.email.error = "Email cannot be empty"

                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()) {
                binding.email.error = "Invalid email address"
                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            // Validate mobile number
            else if (_binding!!.mobileNo.text.toString().isEmpty()) {
                _binding!!.mobileNo.error = "Mobile number cannot be empty"
                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else if (!Patterns.PHONE.matcher(binding.mobileNo.text.toString()).matches()) {
                binding.mobileNo.error = "Invalid mobile number"
                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else if (_binding!!.mobileNo.text.toString().length == 9) {
                _binding!!.mobileNo.error = "enter 10 digit"
                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE}

            // Validate full name
            else if (_binding!!.fullname.text.toString().isEmpty()) {
                _binding!!.fullname.error = "Full name cannot be empty"
                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            // Validate password
            else if (_binding!!.password.text.toString().isEmpty()) {
                _binding!!.password.error = "Password cannot be empty"
                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else if (_binding!!.password.text.toString().length < 8) {
                _binding!!.password.error = "Password should be at least 8 characters long"
                binding.buttonSignUp.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }


           else{

                val regiUser = RegistrationUser(email, mobileNo  , fullName , password)
                val call = AuthApi.retrofitService.registeruser(regiUser)



                call.enqueue(object : retrofit2.Callback<RegistrationResponce>{

                    override fun onResponse(
                        call: Call<RegistrationResponce>,
                        response: Response<RegistrationResponce>
                    ) {

                        if (response.code() == 201) {
                            binding.buttonSignUp.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            Log.e("registration" , " flow")

                            val UserId = response.body()?.data?._id
                            val bundle = Bundle()
                            bundle.putString("Email", binding.email.text.toString())
                            bundle.putBoolean("Registration" , true)
                            bundle.putString("UID", UserId)
                            val fragment: Fragment = Otp()
                            fragment.arguments = bundle

                            Oos.editor!!.putString("Password" , password)
                          val pasd = Oos.sharedPreferences?.contains("Password")
                        Log.e("pass" , pasd.toString())

                            val navController = findNavController()
                            navController.navigate(R.id.action_registration_to_otp,bundle)

                        }else if(response.code() == 400)
                        {
                            binding.buttonSignUp.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context , "alreaday exist" , Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onFailure(call: Call<RegistrationResponce>, t: Throwable) {
                        binding.buttonSignUp.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context , "Something wrong" , Toast.LENGTH_LONG).show()
                    }

                })
            }


        }

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.action_registration_to_login)
        }

        return binding.root
    }
}
