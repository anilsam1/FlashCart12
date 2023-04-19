package com.example.authentication.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.authentication.R
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.model.*

class CartAdapter(private var Cartlistlivedata: LiveData<List<DataXX>?>, private val viewmodel: Productviewmodel) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val image: ImageView = itemView.findViewById(R.id.item_image)
        val text: TextView = itemView.findViewById(R.id.item_name)
        val prize: TextView = itemView.findViewById(R.id.item_prize)
        val quantity: TextView = itemView.findViewById(R.id.quantity_text_view)
       // val description: TextView = itemView.findViewById(R.id.Product_description)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleat)
        private val Increase: Button = itemView.findViewById(R.id.increase)
        private val decrese: Button = itemView.findViewById(R.id.decrese)


        init {
            itemView.setOnClickListener(this)
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Cartlistlivedata.value?.get(position)?._id.let { product ->
                       viewmodel.onDeleteItemClicked(product)
                        Log.e("ProductOne"  , product.toString())

                    }
                }
            }

            Increase.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Cartlistlivedata.value?.get(position)?._id.let { product ->
                        viewmodel.increasequantity(product)
                        Log.e("ProductOne"  , product.toString())
                    }
                }
            }


            decrese.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Cartlistlivedata.value?.get(position)?._id.let { product ->
                      viewmodel.decreasequantity(product)
                        Log.e("ProductOne"  , product.toString())
                    }
                }
            }



        }

        override fun onClick(v: View?) {
            val context = itemView.context
            val intent = Intent(context, CartDetaile::class.java)

            Cartlistlivedata.value?.get(adapterPosition)?.let { product ->
                intent.putExtra("product_name", product.productDetails?.title)
                intent.putExtra("product_image", product.productDetails?.imageUrl)
                intent.putExtra("product_prize", product.productDetails?.price)
                intent.putExtra("product_description", product.productDetails?.description)
                intent.putExtra("product_id", product._id)
                intent.putExtra("isliked", true)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() =
        Cartlistlivedata.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product2 = Cartlistlivedata.value?.get(position)

        Log.e("adapter", product2.toString())

        product2?.let { product ->
            holder.image.load(product.productDetails?.imageUrl)
            holder.text.text = product.productDetails?.title
            holder.prize.text = product.productDetails?.price
      //      holder.description.text = product.productDetails?.description
            holder.quantity.text = product.quantity.toString()

        }
    }

    fun updateData(newcartList: MutableLiveData<List<DataXX>?>) {
        Cartlistlivedata = newcartList
        notifyDataSetChanged()
    }
}