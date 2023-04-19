package com.example.authentication.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.authentication.R
import com.example.authentication.retrofit.model.OrderHistoryResponceData

class Orderadapter(private val Orderlivedata: MutableLiveData<List<OrderHistoryResponceData>?>): RecyclerView.Adapter<Orderadapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {

        val image: ImageView = itemView.findViewById(R.id.item_image)
        val text: TextView = itemView.findViewById(R.id.item_name)
        val time: TextView = itemView.findViewById(R.id.timee)
        val total: TextView = itemView.findViewById(R.id.item_total)
        val date: TextView = itemView.findViewById(R.id.item_date)
        val quentity: TextView = itemView.findViewById(R.id.item_quentity)
        val price: TextView = itemView.findViewById(R.id.item_prize)
        //     val description: TextView = itemView.findViewById(R.id.Product_description)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product2 =Orderlivedata.value?.get(position)

        Log.e("adapter" , product2.toString())

        if (product2 != null) {
            holder.image.load(product2.imageUrl)
            holder.text.text = product2.title
            holder.time.text = product2.updatedAt.substring(11,19)
            holder.price.text = product2.price
            holder.date.text = product2.updatedAt.substring(0,10)
            holder.total.text =product2.productTotalAmount
            holder.quentity.text = product2.quantity.toString()
      //      holder.description.text = product2.title
        }
    }

    override fun getItemCount() =
        Orderlivedata.value?.size ?: 0
    }




