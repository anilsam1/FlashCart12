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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.authentication.R
import com.example.authentication.databinding.ActivityWishlistDetailedBinding
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.AddToCart
import com.example.authentication.retrofit.model.AddToCartResponce
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response
import kotlin.properties.Delegates

class WishlistDetailed : AppCompatActivity() {

    lateinit var binding: ActivityWishlistDetailedBinding
    lateinit var token: String
    lateinit var productName: String
    lateinit var productimage: String
    lateinit var productprize: String
    var cartitemid : String? = null
    lateinit var productdescription: String
    lateinit var productid: String
    lateinit var wishlist: String
    var isLiked by Delegates.notNull<Boolean>()
    private lateinit var productViewModel: Productviewmodel
    private lateinit var networkChangeReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel = ViewModelProvider(this)[Productviewmodel::class.java]
        binding = ActivityWishlistDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Oos.sharedPreferences?.let {
            token = it.getString("JWTTOKEN", null).toString()
        }
        productName = intent.getStringExtra("product_name").toString()
        productimage = intent.getStringExtra("product_image").toString()
        productprize = intent.getStringExtra("product_prize").toString()
        cartitemid = intent.getStringExtra("Cart_Item_id")
        productdescription = intent.getStringExtra("product_description").toString()
        productid = intent.getStringExtra("product_id").toString()
        isLiked = intent.getBooleanExtra("isliked", false)
        wishlist = intent.getStringExtra("watchlist_item_id").toString()

        Log.e("cartitemid" , cartitemid.toString())

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

        if (cartitemid != null) {
            binding.cart.text = "Already in cart"
            binding.cart.isEnabled = false
        } else if (cartitemid == null) {
            binding.cart.text = "Add to cart"
        }
        binding.cart.setOnClickListener {

            binding.cart.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            val add = AddToCart(productid.toString())
            val call = AuthApi.retrofitService.addToCart(add, "Bearer $token")
            Log.e("token", token)
            call.enqueue(object : retrofit2.Callback<AddToCartResponce> {

                override fun onResponse(
                    call: Call<AddToCartResponce>,
                    response: Response<AddToCartResponce>
                ) {
                    Log.e("wishlist", token)
                    Log.e("token", response.code().toString())
                    Log.e("responcecode", response.code().toString())

                    if (response.code() == 201) {

                        binding.cart.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(
                            binding.root,
                            "$productName is added in cart",
                            Snackbar.LENGTH_LONG
                        ).show()

                    } else if (response.code() == 400) {
                        binding.cart.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@WishlistDetailed,
                            "something wents to wrong",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(call: Call<AddToCartResponce>, t: Throwable) {
                    binding.cart.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "$productName is alrady added in your Wishlist",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
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
