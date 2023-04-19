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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.authentication.R
import com.example.authentication.databinding.ActivityDetailBinding
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.*
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    lateinit var token: String
    lateinit var productName: String
    lateinit var productimage: String
    lateinit var productprize: String
    lateinit var productdescription: String
    lateinit var productid: String
    lateinit var wishlist: String

   private var cartitemid: String? = null
    var isLiked by Delegates.notNull<Boolean>()
    private lateinit var productViewModel: Productviewmodel
    private lateinit var networkChangeReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel = ViewModelProvider(this)[Productviewmodel::class.java]
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Oos.sharedPreferences?.let {
            token = it.getString("JWTTOKEN", null).toString()
        }
        productName = intent.getStringExtra("product_name" ).toString()
        cartitemid = intent.getStringExtra("cart_item_id")
        productimage = intent.getStringExtra("product_image").toString()
        productprize = intent.getStringExtra("product_prize").toString()
        productdescription = intent.getStringExtra("product_description").toString()
        productid = intent.getStringExtra("product_id").toString()
        isLiked = intent.getBooleanExtra("isliked", false)
        wishlist = intent.getStringExtra("watchlist_item_id").toString()



        binding.tvProductName.text = productName
        binding.itemPrize.text = productprize
        binding.tvDescription.text = productdescription
        binding.imageFood.load(productimage)

        val drawableRes = if (isLiked) R.drawable.heart else R.drawable.emptyheart
        val icon = ContextCompat.getDrawable(this@DetailActivity, drawableRes)
        binding.like.setImageDrawable(icon)

        binding.like.setOnClickListener {
            if (isLiked) {
                binding.like.setImageResource(R.drawable.emptyheart)
                productViewModel.removefromwishlist(wishlist)
                productViewModel.getwishlist(token)
            } else {
                binding.like.setImageResource(R.drawable.heart)
                productViewModel.addtowishlist(productid)
                productViewModel.getwishlist(token)
            }
            isLiked = !isLiked
        }

        binding.arrow.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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

            call.enqueue(object : retrofit2.Callback<AddToCartResponce> {

                override fun onResponse(
                    call: Call<AddToCartResponce>,
                    response: Response<AddToCartResponce>
                ) {


                    if (response.code() == 201) {

                        binding.cart.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
//                        binding.cart.text = "Already added"
//                        binding.cart.isClickable = false
                        Snackbar.make(
                            binding.root,
                            "$productName is added in cart",
                            Snackbar.LENGTH_LONG
                        ).show()

                    } else if (response.code() == 400) {
                        binding.cart.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@DetailActivity,
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

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

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






