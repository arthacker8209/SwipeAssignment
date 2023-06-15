package com.example.swipeassignment.feature.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swipeassignment.databinding.ProductItemBinding
import com.example.swipeassignment.network.models.ProductListing

class ProductAdapter(private val onItemClick: (ProductListing) -> Unit)
    : ListAdapter<ProductListing, ProductAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL =
            object : DiffUtil.ItemCallback<ProductListing>() {
                override fun areItemsTheSame(
                    oldItem: ProductListing,
                    newItem: ProductListing
                ) = oldItem.productName == newItem.productName

                override fun areContentsTheSame(
                    oldItem: ProductListing,
                    newItem: ProductListing
                ): Boolean {
                    return oldItem.productName == newItem.productName && oldItem.productType ==
                            newItem.productType
                }
            }
    }

    inner class ViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                binding.productModel?.let {
                    onItemClick(it)
                }
            }
        }

        fun bind(productEntity: ProductListing) {
            binding.productModel = productEntity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}