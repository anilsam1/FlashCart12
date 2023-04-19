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
import com.example.authentication.databinding.FragmentNewpasswordBinding
import com.example.authentication.home.HomeActivity
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.ChangePassword
import com.example.authentication.retrofit.model.LoginUser
import com.example.authentication.retrofit.model.RegistrationResponce
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [newpassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class newpassword : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

return view



    }
}

