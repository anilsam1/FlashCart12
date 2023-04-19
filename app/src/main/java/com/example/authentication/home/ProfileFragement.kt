package com.example.authentication.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.authentication.MainActivity
import com.example.authentication.R
import com.example.authentication.authentication.NewPass
import com.example.authentication.databinding.FragmentProfileFragementBinding
import java.net.URI


class ProfileFragement : Fragment() {

    private var _binding: FragmentProfileFragementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        _binding = FragmentProfileFragementBinding.inflate(inflater, container, false)

        val encodedImage = Oos.sharedPreferences?.getString("profileImage", "")
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        binding.profilePicture.setImageBitmap(bitmap)

        if (bitmap == null){
            binding.profilePicture.setImageResource(R.drawable.profile)
        }

        binding.edit.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Edit Profile?")
            builder.setMessage("Would you like to edit your profile picture? You can choose to upload a new image, remove your current profile picture, or cancel the action.")
            builder.setIcon(R.drawable.ic_persone)
            builder.setPositiveButton("Yes"){dialogInterface, which ->
            uploadimag(binding.profilePicture)
            }
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
            }
            builder.setNegativeButton("Remove Profile"){dialogInterface, which ->
                Oos.editor?.remove("profileImage")
                Oos.editor?.apply()
                binding.profilePicture.setImageResource(R.drawable.profile)


            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()


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







//        val uri = Uri.parse(image)
//        Log.e("uri" , uri.toString())

//
//        if(uri != null){
//            binding.profilePicture.setImageURI(uri)
//        }else if (uri == null){
//
//        }


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
            val inputStream = activity?.contentResolver?.openInputStream(data?.data!!)
            val imageBytes = inputStream?.readBytes()
            val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            Oos.editor?.putString("profileImage", encodedImage)
            Oos.editor?.apply()


            Log.e("data" , data?.data.toString())
            binding.profilePicture.setImageURI(data?.data)
        }
    }


}
