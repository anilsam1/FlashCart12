package com.example.authentication.retrofit

import android.util.Log
import com.example.authentication.authentication.NewPass
import com.example.authentication.retrofit.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private val BASE_URL =
    "https://shopping-app-backend-t4ay.onrender.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())      //Json adapter Factory
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))    //serialize and deserialize JSON to Kotlin objects.
    .baseUrl(BASE_URL)
    .build()

interface AuthenticationApi {

    @POST("user/registerUser")
    fun registeruser(@Body registerUser: RegistrationUser ): Call<RegistrationResponce>


    @POST("user/verifyOtpOnRegister")
    fun verifyOtp(@Body otpRequestData: RegistrationOtp): Call<RegistrationResponce>

    @POST("user/login")
    fun loginUser(@Body registerUserData: LoginUser): Call<RegistrationResponce>

    @POST("user/forgotPassword")
    fun forgotPassword(@Body forgot: Forgot): Call<RegistrationResponce>

    @POST("/user/resendOtp")
    fun resendotp(@Body resendotp: Resendotp): Call<ResendotpResponce>

    @POST("user/verifyOtpOnForgotPassword")
    fun verifyOtpOnForgot(@Body otpRequestData: RegistrationOtp): Call<RegistrationResponce>

    @POST("user/resendOtp")
    fun resendOtp(@Body otpRequestData: otpRequestData): Call<RegistrationResponce>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/changePassword")
    fun changPassword(@Body changePassword: ChangePassword,@Header("Authorization") jwtToken:String): Call<RegistrationResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/product/getAllProduct")
    fun getAllProducts(@Header("Authorization") jwtToken:String):Call<Productresponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("watchList/addToWatchList")
    fun addToWatchList(@Body watchlist : Watchlist ,@Header("Authorization") jwtToken:String): Call<AddWishlistResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/watchList/getWatchList")
    fun getWishList(@Header("Authorization") jwtToken:String):Call<WatchlistResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/watchList/removeFromWatchList")
    fun removefromwishlist(@Body removeFromWishlist: RemoveFromWishlist ,@Header("Authorization") jwtToken:String): Call<RemoveWishlistResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/addToCart")
    fun addToCart(@Body AddToCart : AddToCart ,@Header("Authorization") jwtToken:String): Call<AddToCartResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/cart/getMyCart")
    fun getCartlist(@Header("Authorization") jwtToken:String):Call<GetMyCartResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/removeProductFromCart")
    fun removefromcart(@Body RemoveFromCart : RemoveFromCart ,@Header("Authorization") jwtToken:String): Call<RemoveFromCartResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/increaseProductQuantity")
    fun increasequntity(@Body IncreaseQuntity : IncreaseProductQuntity , @Header("Authorization") jwtToken: String):Call<IncreaseQuntityResponce>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/decreaseProductQuantity")
    fun decreasequntity(@Body IncreaseQuntity : DecreaseProduct, @Header("Authorization") jwtToken: String):Call<DecreaseQuntityResponce>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/order/placeOrder")
    fun placeorders(@Body PlaceOrder : PlaceOrder, @Header("Authorization") jwtToken: String):Call<PlaceOrderResponce>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/order/getOrderHistory")
    fun getOrderHistory(@Header("Authorization") jwtToken:String):Call<OrderHistoryResponce>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/user/changePassword")
    fun newpassword(@Body newpass : newpass , @Header("Authorization") jwtToken: String):Call<NewpassResponce>

}

object AuthApi{
    val retrofitService:  AuthenticationApi by lazy {
        Log.e("Error" , "retrofit")
        retrofit.create(AuthenticationApi ::class.java)
    }
}