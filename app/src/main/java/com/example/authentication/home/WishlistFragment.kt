package com.example.authentication.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authentication.databinding.FragmentWishlistBinding

import com.example.authentication.home.viewmodels.Productviewmodel

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var productViewModel: Productviewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val view = binding.root



        productViewModel = ViewModelProvider(this)[Productviewmodel::class.java]
        val recyclerView = binding.recyclerViewWishlist
        val adapter = WishListAdapter(productViewModel.wishlist , productViewModel)
          recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager


        Log.e("Wishlist_fagment" , " hear")
        productViewModel.wishlist.observe(viewLifecycleOwner, Observer { wishlistItems ->
            adapter.notifyDataSetChanged()
            binding.overlay.visibility = View.GONE

           if (wishlistItems.isEmpty()){
               binding.overlay2.visibility = View.VISIBLE
           }else{
               binding.overlay2.visibility = View.GONE
           }


        })

        return view
    }

}