package com.example.authentication.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.authentication.MainActivity
import com.example.authentication.authentication.NewPass
import com.example.authentication.databinding.FragmentProfileFragementBinding


class ProfileFragement : Fragment() {

    private var _binding: FragmentProfileFragementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        _binding = FragmentProfileFragementBinding.inflate(inflater, container, false)


        binding.profilePicture.setOnClickListener{
       //     uploadimag(binding.profilePicture)

        }

        Oos.sharedPreferences?.let {
            if (it.contains("NAME")) {
                val x = it.getString("NAME", null).toString()
                binding.ProfileName.text = x
            }
        }

        binding.OrderhistoryCardview.setOnClickListener {
            val intent = Intent(requireContext(), OrderHistory::class.java)
            startActivity(intent)
        }



        binding.ChangepasswordCardview.setOnClickListener {
            val intent = Intent(requireContext(), NewPass::class.java)
            startActivity(intent)
        }


        binding.LogoutCardview.setOnClickListener {
            Oos.editor?.clear()?.commit()
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        if (Oos.sharedPreferences!!.getBoolean("On", false)) {
            binding.switch1.isChecked = true
        }

        binding.switch1.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                Oos.editor!!.putBoolean("On", true).commit()
            } else {
                Oos.editor!!.putBoolean("On", false).commit()
            }

        }




        Log.e("sdfasf" , Oos.sharedPreferences!!.getBoolean("On" , true).toString())

        return binding.root
    }

private fun uploadimag(image : ImageView){
val intent = Intent()
    intent.action = Intent.ACTION_GET_CONTENT
    intent.type = "image/*"
    startActivityForResult(intent , 1)
}




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            binding.profilePicture.setImageURI(data?.data)
        }
    }
}
