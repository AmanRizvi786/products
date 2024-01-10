package com.example.projectfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.textViewProductName)
        val productPrice: TextView = itemView.findViewById(R.id.textViewProductPrice)
        val productDescription: TextView = itemView.findViewById(R.id.textViewProductDescription)
        val productImage: ImageView = itemView.findViewById(R.id.imageViewProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_product, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = productList[position]

        holder.productName.text = currentProduct.productName
        holder.productPrice.text = currentProduct.productPrice
        holder.productDescription.text = currentProduct.productDescription

        // Load the image using Glide or your preferred image loading library
        Glide.with(holder.itemView.context)
            .load(currentProduct.productImage)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
