package com.example.authentication.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.authentication.databinding.FragmentHomeFragementBinding
import com.example.authentication.home.viewmodels.Productviewmodel
import com.example.authentication.retrofit.model.DataX


class HomeFragement : Fragment() , Onbackpress {


    private var _binding: FragmentHomeFragementBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: Productviewmodel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeFragementBinding.inflate(inflater, container, false)
        val view = binding.root

        val token =
            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
        val name =
            Oos.sharedPreferences!!.getString("NAME", null).toString()
        productViewModel = ViewModelProvider(this)[Productviewmodel::class.java]
        productViewModel.getProductData(token , requireContext())

        val list: MutableList<DataX> = emptyList<DataX>().toMutableList()
        productViewModel.productList.observe(viewLifecycleOwner) {
            list.clear()
            it.forEach {

                list += it
            }
        }


        val recyclerView = binding.recyclerView
        val adapter =
            ProductAdapter(
                list, productViewModel, HomeActivity(),
                requireActivity() as HomeActivity,
                requireContext()
            )
        recyclerView.adapter = adapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        productViewModel.productList.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
            binding.overlay.visibility = View.GONE

        })




        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val list: MutableList<DataX> = emptyList<DataX>().toMutableList()

                productViewModel.productList.observe(viewLifecycleOwner) {
                    it.forEach {
                        if (it.title?.lowercase()!!.contains(p0!!.lowercase().toString())) {
                            list += it
                        }
                    }

                    val adapter =
                        ProductAdapter(
                            list, productViewModel, HomeActivity(),
                            requireActivity() as HomeActivity,requireContext()
                        )
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                return false
            }

        })
        return view

    }

//    override fun onResume() {
//        super.onResume()
//        val token =
//            Oos.sharedPreferences!!.getString("JWTTOKEN", null).toString()
//        productViewModel.getProductData(token , requireContext())
//    }

    override fun OnBackPressedCallback() {

    }

}


