package com.example.authentication.home


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.authentication.R
import com.example.authentication.databinding.ActivityHomeBinding
class HomeActivity : AppCompatActivity() , Lisner {
    private lateinit var networkChangeReceiver: BroadcastReceiver
    lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout2, HomeFragement())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            Log.d("HomeActivity", "Selected item ID: ${menuItem.itemId}")
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.home -> {
                    Log.d("HomeActivity", "HomeFragment selected")
                    selectedFragment = HomeFragement()
                }
                R.id.cart -> {
                    Log.d("HomeActivity", "CartFragment selected")
                    selectedFragment = CartFragrment()
                }
                R.id.order -> {
                    Log.d("HomeActivity", "WishlistFragment selected")
                    selectedFragment = WishlistFragment()
                }
                R.id.profile -> {
                    Log.d("HomeActivity", "ProfileFragment selected")
                    selectedFragment = ProfileFragement()
                }
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout2, selectedFragment)
                    .commit()
                true
            } else {
                false
            }
        }


        networkChangeReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onReceive(context: Context?, intent: Intent?) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_DATA_USAGE_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(intent)
                    return
                }
            }
        }
        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

    }
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout2)
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()


        if (currentFragment is HomeFragement) {
            finishAffinity()
        } else {
            fragmentTransaction.replace(R.id.frameLayout2, HomeFragement()).commit()
            binding.bottomNavigation.selectedItemId = R.id.home
        }



    }

    override fun onclicklisner(position: Int) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout2)
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout2, WishlistFragment()).commit()
        binding.bottomNavigation.selectedItemId = R.id.order





    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}

