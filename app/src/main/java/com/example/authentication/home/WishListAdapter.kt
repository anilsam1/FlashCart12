package com.example.authentication.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.authentication.R
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.AuthApi
import com.example.authentication.retrofit.model.DataX
import com.example.authentication.retrofit.model.DataXX
import com.example.authentication.retrofit.model.RemoveFromWishlist
import com.example.authentication.retrofit.model.RemoveWishlistResponce
import retrofit2.Call
import retrofit2.Response

class WishListAdapter(private var WishListLiveData : LiveData<List<DataX>>, private val viewmodel: Productviewmodel) : RecyclerView.Adapter<WishListAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {

        val image: ImageView = itemView.findViewById(R.id.item_image)
        val text: TextView = itemView.findViewById(R.id.item_name)
        val prize: TextView = itemView.findViewById(R.id.item_prize)
        private val deleat: ImageView = itemView.findViewById(R.id.deleat2)
    //    val description: TextView = itemView.findViewById(R.id.Product_description)

        init {
            itemView.setOnClickListener(this)
            deleat.setOnClickListener{
                val token = Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
                val position = adapterPosition
                Log.e("remove" , position.toString())
                if (position != RecyclerView.NO_POSITION) {
                    val product = WishListLiveData.value?.get(position)?._id
                    viewmodel.getwishlist(token)
                    Log.e("remove" , product.toString())
                        viewmodel.removefromwishlist(product.toString())

                    }

                }}

        override fun onClick(v: View?) {

            val context = itemView.context
            val intent = Intent(context, WishlistDetailed::class.java)
            intent.putExtra("product_name",
                WishListLiveData.value?.get(adapterPosition)?.productDetails?.title!!
            )

            intent.putExtra("product_image",
                WishListLiveData.value?.get(adapterPosition)?.productDetails?.imageUrl
            )
            intent.putExtra("Cart_Item_id",
                WishListLiveData.value?.get(adapterPosition)?.cartItemId
            )
            intent.putExtra("product_prize",
                WishListLiveData.value?.get(adapterPosition)?.productDetails?.price
            )
            intent.putExtra("product_description",
                WishListLiveData.value?.get(adapterPosition)?.productDetails?.description
            )
            intent.putExtra("product_id", WishListLiveData.value!!.get(adapterPosition).productDetails?._id)
            intent.putExtra("watchlist_item_id", WishListLiveData.value!!.get(adapterPosition).watchListId)
            Log.e("wishlist" ,
                WishListLiveData.value!!.get(adapterPosition).watchListId.toString()
            )
            intent.putExtra("isliked", true)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wishlist_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() =
        WishListLiveData.value?.size ?: 0


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product2 =WishListLiveData.value?.get(position)

Log.e("adapter" , product2.toString())

        if (product2 != null) {
            holder.image.load(product2.productDetails?.imageUrl)
            holder.text.text = product2.productDetails?.title
            holder.prize.text = product2.productDetails?.price
      //      holder.description.text = product2.productDetails?.title
        }
    }

    fun updateData(newWishList: MutableLiveData<List<DataX>>) {
        WishListLiveData = newWishList
        notifyDataSetChanged()
    }
}