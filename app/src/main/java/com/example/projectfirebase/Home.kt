package com.example.projectfirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: MutableList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerViewProducts)
        productList = mutableListOf()
        productAdapter = ProductAdapter(productList)

        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = productAdapter

        // Load data from Firestore
        loadProducts()

        return rootView
    }

    private fun loadProducts() {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val productName = document.getString("Product Name") ?: ""
                    val productPrice = document.getString("Product Price") ?: ""
                    val productDescription = document.getString("Product Description") ?: ""
                    val productImage = document.getString("Product Image") ?: ""

                    val product = Product(
                        productName,
                        productPrice,
                        productDescription,
                        productImage
                    )

                    productList.add(product)
                }

                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }
}
