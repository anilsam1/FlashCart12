package com.example.authentication.walkthroughscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.authentication.MainActivity
import com.example.authentication.R
import com.example.authentication.authentication.Login
import com.example.authentication.databinding.ActivityOrderHistoryBinding
import com.example.authentication.databinding.ActivityWalkthroughtBinding
import com.example.authentication.home.HomeActivity
import com.example.authentication.home.Oos
import com.example.authentication.home.OrderHistory
import com.example.authentication.home.WalkthroughOos
import com.example.authentication.retrofit.model.LoginUser


lateinit var binding : ActivityWalkthroughtBinding
private lateinit var Onboardingitemadapter: onboardingitemadapter
private lateinit var indicatorsContainer: LinearLayout

class Walkthrought : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWalkthroughtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WalkthroughOos.sharedPreferences?.let {
            var a = WalkthroughOos.sharedPreferences?.getString("wa" , null)
            Log.e(" asdfasf" , a.toString())
            if (it.contains("wa")) {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }





        binding.buttonGetStarted.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            WalkthroughOos.editor?.putString("wa" , "aa")
            startActivity(intent)
        }
        binding.textSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        setOnboardingItems()
    }
        private fun setOnboardingItems() {
            Onboardingitemadapter = onboardingitemadapter(
                listOf(
                    OnboardingItem(
                        onboardingImage = R.raw.easyshopping ,
                        title = "Easy Shopping",
                        description = " dfasfdasfd"
                    ),
                    OnboardingItem(
                        onboardingImage = R.raw.securepayment ,
                        title = "Secure Payment",
                        description = " dfasfdasfd"
                    ), OnboardingItem(
                        onboardingImage = R.raw.quickdelevery ,
                        title = "Quick Delivery",
                        description = " dfasfdasfd"
                    )

                )
            )
     val onboardingViewpager = findViewById<ViewPager2>(R.id.onboardingViewPager)
            onboardingViewpager.adapter = Onboardingitemadapter



    }





}