package com.example.authentication.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authentication.R
import com.example.authentication.databinding.FragmentCartFragrmentBinding
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.PlaceOrder
import com.example.authentication.retrofit.model.PlaceOrderResponce
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private var _binding: FragmentCartFragrmentBinding? = null
private val binding get() = _binding!!
private lateinit var productViewModel: Productviewmodel



class CartFragrment : Fragment() {
    var cartTotal by Delegates.notNull<Double>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartFragrmentBinding.inflate(inflater, container, false)
        val view = binding.root

        productViewModel = ViewModelProvider(this)[Productviewmodel::class.java]
        val recyclerView = binding.recyclerViewCartlist
        val adapter = CartAdapter(productViewModel.cartlist, productViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        productViewModel.cartlist2.observe(viewLifecycleOwner) {
            val text = if (it != null) String.format("%.2f", it) else "0"
            binding.prize.text = text
        }



        productViewModel.cartlist.observe(viewLifecycleOwner, Observer {
            Log.e("InCartFragmrnt", productViewModel.cartlist.toString())
            adapter.notifyDataSetChanged()
            binding.overlay.visibility = View.GONE

            if (it.isNullOrEmpty()) {
                binding.overlay2.visibility = View.VISIBLE
            } else {
                binding.overlay2.visibility = View.GONE
            }



            binding.placeorder.setOnClickListener {

                binding.progressBar.visibility = View.VISIBLE
                binding.placeorder.visibility = View.GONE

                val carttotal = productViewModel.cartlist2.value
                val cartid = productViewModel.cartlist.value?.get(0)?.cartId.toString()
                val token =
                    Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
                val place = PlaceOrder(cartid, carttotal.toString())
                val callApi = AuthApi.retrofitService.placeorders(place, "Bearer $token")

                callApi.enqueue(object : Callback<PlaceOrderResponce> {
                    override fun onResponse(
                        call: Call<PlaceOrderResponce>,
                        response: Response<PlaceOrderResponce>
                    ) {
                        if (response.code() == 201) {
                            binding.progressBar.visibility = View.GONE
                            binding.placeorder.visibility = View.VISIBLE
                            productViewModel.getCartlist(token)
                            Log.e("Sucessfully addes", "done")
                            binding.prize.setText("0")

                            if (Oos.sharedPreferences!!.getBoolean("On", false)) {
                                showNotification()
                            }




                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.placeorder.visibility = View.VISIBLE
                            Log.e("getcartlist", "Error fetching products: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<PlaceOrderResponce>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        binding.placeorder.visibility = View.VISIBLE
                    }
                })
            }


        })




        return view
    }

    private fun showNotification() {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "cart_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Cart", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(requireContext(), OrderHistory::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Order Confirm")
            .setContentText("We have received your order number and your order is being processed")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1, notificationBuilder.build())
    }


}
