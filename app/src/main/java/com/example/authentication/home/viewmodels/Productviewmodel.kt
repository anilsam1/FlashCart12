package com.example.authentication.home.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.MainActivity
import com.example.authentication.authentication.NewPass
import com.example.authentication.home.Oos

import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Productviewmodel : ViewModel() {

    private val _productList = MutableLiveData<List<DataX>>()
    val productList: MutableLiveData<List<DataX>>
        get() = _productList


    private val _wishlist = MutableLiveData<List<DataX>>()
    val wishlist: MutableLiveData<List<DataX>>
        get() = _wishlist

    private val _cartlist = MutableLiveData<List<DataXX>?>()
    val cartlist: MutableLiveData<List<DataXX>?>
        get() = _cartlist


    private val _orderhistory = MutableLiveData<List<OrderHistoryResponceData>?>()
    val orderhistory: MutableLiveData<List<OrderHistoryResponceData>?>
        get() = _orderhistory


    private val _cartlist2 = MutableLiveData<Double?>()
    val cartlist2: MutableLiveData<Double?>
        get() = _cartlist2


    private val _wishlistid = MutableLiveData<List<DataX>?>()
    val wishlistid: MutableLiveData<List<DataX>?>
        get() = _wishlistid

    val home = Intent().getBooleanExtra("Homefragment", true)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            Oos.sharedPreferences?.let {
                Log.e("ProductViewModel", "Fetching products")
                if (it.contains("JWTTOKEN")) {
                    val token = it.getString("JWTTOKEN", null).toString()
                    Log.e("ProductViewModel", "Token: $token")
                    getwishlist(token)
                    getCartlist(token)
                    getorderhistory(token)


                } else {
                    Log.e("ProductViewModel", "JWT token not found")
                }
            }
        }
    }

    fun getProductData(token: String, context: Context) {
        Log.e("ProductViewModel", "Getting product data")
        val call = AuthApi.retrofitService.getAllProducts("Bearer $token")
        call.enqueue(object : Callback<Productresponce> {
            var productdata: List<DataX> = emptyList()

            override fun onResponse(
                call: Call<Productresponce>,
                response: Response<Productresponce>
            ) {
                if (response.code() == 200) {
                    _productList.value = response.body()?.data!!
                } else if (response.code() == 500) {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                    Oos.editor?.clear()?.commit()
                }
            }

            override fun onFailure(call: Call<Productresponce>, t: Throwable) {
            }
        })
    }


    fun getwishlist(token: String) {
        val call = AuthApi.retrofitService.getWishList("Bearer $token")
        call.enqueue(object : Callback<WatchlistResponce> {


            override fun onResponse(
                call: Call<WatchlistResponce>,
                response: Response<WatchlistResponce>
            ) {
                if (response.code() == 200) {
                    _wishlist.value = response.body()?.data
                    Log.e("getwishlist", " succesfully getwishlist")

                } else {
                    Log.e("ProductViewModel", "Error fetching products: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WatchlistResponce>, t: Throwable) {
                Log.e("ProductViewModel", "Failed to get product data", t)
            }


        })
    }

    fun getCartlist(token: String) {
        val call = AuthApi.retrofitService.getCartlist("Bearer $token")
        call.enqueue(object : Callback<GetMyCartResponce> {


            override fun onResponse(
                call: Call<GetMyCartResponce>,
                response: Response<GetMyCartResponce>

            ) {
                Log.e("getcart", response.code().toString())
                if (response.code() == 200) {
                    _cartlist.value = response.body()?.data
                    _cartlist2.value = response.body()?.cartTotal
                    Log.e("carttotal", cartlist2.value.toString())
                    Log.e("cartlist", cartlist.value.toString())

                } else {
                    Log.e("getcartlist", "Error fetching products: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetMyCartResponce>, t: Throwable) {
                Log.e("ProductViewModel", "Failed to get product data")
            }


        })
    }

    fun onDeleteItemClicked(id: String?) {
        val token =
            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
        val removeFromCartObj = RemoveFromCart(id.toString())
        val callApi = AuthApi.retrofitService.removefromcart(removeFromCartObj, "Bearer $token")

        callApi.enqueue(object : Callback<RemoveFromCartResponce> {
            override fun onResponse(
                call: Call<RemoveFromCartResponce>,
                response: Response<RemoveFromCartResponce>
            ) {
                if (response.code() == 200) {
                    getCartlist(token)
                } else {
                    Log.e("getcartlist", "Error fetching products: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RemoveFromCartResponce>, t: Throwable) {

            }
        })


    }

    fun increasequantity(id: String?) {
        val token =
            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
        val increase = IncreaseProductQuntity(id.toString())
        val callApi = AuthApi.retrofitService.increasequntity(increase, "Bearer $token")

        callApi.enqueue(object : Callback<IncreaseQuntityResponce> {
            override fun onResponse(
                call: Call<IncreaseQuntityResponce>,
                response: Response<IncreaseQuntityResponce>
            ) {
                if (response.code() == 200) {
                    getCartlist(token)
                } else {
                    Log.e("getcartlist", "Error fetching products: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<IncreaseQuntityResponce>, t: Throwable) {

            }
        })

    }


    fun decreasequantity(id: String?) {
        val token =
            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
        val decrease = DecreaseProduct(id.toString())
        val callApi = AuthApi.retrofitService.decreasequntity(decrease, "Bearer $token")

        callApi.enqueue(object : Callback<DecreaseQuntityResponce> {
            override fun onResponse(
                call: Call<DecreaseQuntityResponce>,
                response: Response<DecreaseQuntityResponce>
            ) {
                if (response.code() == 200) {
                    getCartlist(token)

                } else {
                    Log.e("getcartlist", "Error fetching products: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DecreaseQuntityResponce>, t: Throwable) {

            }
        })
    }


    fun getorderhistory(token: String) {
        val call = AuthApi.retrofitService.getOrderHistory("Bearer $token")
        call.enqueue(object : Callback<OrderHistoryResponce> {


            override fun onResponse(
                call: Call<OrderHistoryResponce>,
                response: Response<OrderHistoryResponce>

            ) {
                Log.e("getcart", response.code().toString())
                if (response.code() == 200) {
                    _orderhistory.value = response.body()?.data
                    Log.e("successsfully getorderhidtory", response.toString())


                } else {

                }
            }

            override fun onFailure(call: Call<OrderHistoryResponce>, t: Throwable) {

            }


        })
    }


    fun addtowishlist(Product: String) {
        val token =
            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
        val add = Watchlist(Product)
        Log.e("addtowishlist", Product)
        val call = AuthApi.retrofitService.addToWatchList(add, "Bearer $token")
        call.enqueue(object : retrofit2.Callback<AddWishlistResponce> {
            override fun onResponse(
                call: Call<AddWishlistResponce>,
                response: Response<AddWishlistResponce>
            ) {
                if (response.code() == 200) {
                    getwishlist(token)

                } else if (response.code() == 400) {

                }
            }

            override fun onFailure(
                call: Call<AddWishlistResponce>,
                t: Throwable
            ) {

            }
        })


    }

    fun removefromwishlist(Product: String) {
        val token =
            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
        val remove = RemoveFromWishlist(Product)
        val call =
            AuthApi.retrofitService.removefromwishlist(remove, "Bearer $token")
        call.enqueue(object : retrofit2.Callback<RemoveWishlistResponce> {
            override fun onResponse(
                call: Call<RemoveWishlistResponce>,
                response: Response<RemoveWishlistResponce>
            ) {
                if (response.code() == 200) {
                    getwishlist(token)

                } else if (response.code() == 400) {
                }
            }

            override fun onFailure(
                call: Call<RemoveWishlistResponce>,
                t: Throwable
            ) {

            }
        })
    }

    fun addtocare(productid: String) {
        val token =
            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
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
                    Log.e("addtocart", "successfull")

                } else if (response.code() == 400) {
                    Log.e("addtocart", " not successfull")

                }
            }

            override fun onFailure(call: Call<AddToCartResponce>, t: Throwable) {
                Log.e("addtocart", "else")
            }
        })
    }

}








