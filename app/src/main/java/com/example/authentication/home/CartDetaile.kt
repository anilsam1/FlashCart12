package com.example.authentication.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.authentication.R
import com.example.authentication.databinding.ActivityCartDetaileBinding
import com.example.authentication.databinding.ActivityDetailBinding
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.AddToCart
import com.example.authentication.retrofit.model.AddToCartResponce
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response
import kotlin.properties.Delegates

class CartDetaile : AppCompatActivity() {

    lateinit var binding: ActivityCartDetaileBinding
    lateinit var token: String
    lateinit var productName: String
    lateinit var productimage: String
    lateinit var productprize: String
    lateinit var productdescription: String
    lateinit var productid: String
    lateinit var wishlist: String
    var isLiked by Delegates.notNull<Boolean>()
    private lateinit var productViewModel: Productviewmodel
    private lateinit var networkChangeReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_detaile)


        productViewModel = ViewModelProvider(this)[Productviewmodel::class.java]
        binding = ActivityCartDetaileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Oos.sharedPreferences?.let {
            token = it.getString("JWTTOKEN", null).toString()
        }
        productName = intent.getStringExtra("product_name").toString()
        productimage = intent.getStringExtra("product_image").toString()
        productprize = intent.getStringExtra("product_prize").toString()
        productdescription = intent.getStringExtra("product_description").toString()
        productid = intent.getStringExtra("product_id").toString()
        isLiked = intent.getBooleanExtra("isliked", false)
        wishlist = intent.getStringExtra("watchlist_item_id").toString()

        Log.e("wishlist", token)


        binding.tvProductName.text = productName
        binding.itemPrize.text = productprize
        binding.tvDescription.text = productdescription
        binding.imageFood.load(productimage)

        binding.arrow.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }


    }
        networkChangeReceiver = object : BroadcastReceiver() {
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
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

}