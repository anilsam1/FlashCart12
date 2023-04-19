package com.example.authentication.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.authentication.R
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.*
import retrofit2.Call
import retrofit2.Response


class ProductAdapter(
    private val productListLiveData: List<DataX>,
    private val viewmodel: Productviewmodel,
    private val activity: HomeActivity,
    private val lisner: HomeActivity,
    private val Context: Context
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        val image: ImageView = itemView.findViewById(R.id.item_image)
        val text: TextView = itemView.findViewById(R.id.item_name)
        val prize: TextView = itemView.findViewById(R.id.item_prize)
        val like: ImageView = itemView.findViewById(R.id.like)
        val addToCart: Button = itemView.findViewById(R.id.addtocart)
        //     val description: TextView = itemView.findViewById(Product_description)


        init {
            itemView.setOnClickListener(this)


            val token =
                Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
            viewmodel.getProductData(token, Context)
            addToCart.setOnClickListener {

                if (productListLiveData.get(adapterPosition).cartItemId != null) {
                    addToCart.isClickable = false
                } else {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val product = productListLiveData.get(position)
                        product.let { p ->
                            Log.e("Productid", p._id.toString())
                            viewmodel.addtocare(p._id.toString())
                            val token =
                                Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
                            viewmodel.getProductData(token, Context)
                            val intent = Intent(Context, HomeActivity::class.java)
                            Context.startActivity(intent)
                        }
                    }
                }
            }

            like.setOnClickListener {

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productListLiveData.get(position)
                    product?.let { p ->
                        val token = Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
                        if (p.watchListItemId != null) {
                            // Remove from wishlist
                            val remove = RemoveFromWishlist(p.watchListItemId)
                            Log.e("addtowishlist", p.watchListItemId)
                            val call =
                                AuthApi.retrofitService.removefromwishlist(remove, "Bearer $token")
                            call.enqueue(object : retrofit2.Callback<RemoveWishlistResponce> {
                                override fun onResponse(
                                    call: Call<RemoveWishlistResponce>,
                                    response: Response<RemoveWishlistResponce>
                                ) {
                                    if (response.code() == 200) {
                                        p.isWishlisted = false
                                        viewmodel.getwishlist(token)
                                        Log.e(
                                            "watchlistidr",
                                            viewmodel.productList.value?.get(position)?.watchListItemId.toString()
                                        )

                                        like.setImageResource(R.drawable.emptyheart)


                                        Log.e("removefromwishlist", response.code().toString())
                                    } else if (response.code() == 400) {
                                        // Handle error
                                    }
                                }

                                override fun onFailure(
                                    call: Call<RemoveWishlistResponce>,
                                    t: Throwable
                                ) {

                                }
                            })
                        } else {
                            like.setImageResource(R.drawable.heart)
                            lisner.onclicklisner(adapterPosition)
                            val token =
                                Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
                            viewmodel.getwishlist(token)
                            val add = Watchlist(p._id.toString())
                            Log.e("addtowishlist", p._id.toString())
                            val call = AuthApi.retrofitService.addToWatchList(add, "Bearer $token")
                            call.enqueue(object : retrofit2.Callback<AddWishlistResponce> {
                                override fun onResponse(
                                    call: Call<AddWishlistResponce>,
                                    response: Response<AddWishlistResponce>
                                ) {
                                    if (response.code() == 200) {
                                        p.isWishlisted = true
                                        Log.e(
                                            "watchlistids",
                                            viewmodel.productList.value?.get(position)?.watchListItemId.toString()
                                        )
                                        viewmodel.getwishlist(token)
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
                    }
                }


            }


        }

        override fun onClick(v: View?) {

            val context = itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("product_name", productListLiveData.get(adapterPosition).title)
            intent.putExtra("cart_item_id", productListLiveData.get(adapterPosition).cartItemId)
            intent.putExtra("product_image", productListLiveData.get(adapterPosition).imageUrl)
            intent.putExtra("product_prize", productListLiveData.get(adapterPosition).price)
            intent.putExtra(
                "product_description",
                productListLiveData.get(adapterPosition)?.description
            )
            intent.putExtra("product_id", productListLiveData.get(adapterPosition)?._id)
            intent.putExtra(
                "watchlist_item_id",
                productListLiveData.get(adapterPosition)?.watchListItemId
            )
            intent.putExtra("isliked", productListLiveData.get(adapterPosition).isWishlisted)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productListLiveData.get(position)

        if (product != null) {
            holder.image.load(product.imageUrl)
            holder.text.text = product.title
            holder.prize.text = product.price.toString()

            if (product.cartItemId != null) {
                holder.addToCart.setText("Already added")
                holder.addToCart.setBackgroundColor(ContextCompat.getColor(Context, R.color.white))

            } else {
                holder.addToCart.setText("Add to cart")
                holder.addToCart.setBackgroundColor(
                    ContextCompat.getColor(
                        Context,
                        R.color.levender
                    )
                )
            }

            if (product.isWishlisted == true) {
                holder.like.setImageResource(R.drawable.heart)


            } else {
                holder.like.setImageResource(R.drawable.emptyheart)
            }
        }
    }


    override fun getItemCount() = productListLiveData.size ?: 0

}
