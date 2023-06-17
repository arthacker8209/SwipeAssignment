package com.example.swipeassignment.feature.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swipeassignment.databinding.ProductItemBinding
import com.example.swipeassignment.network.models.Product

class ProductAdapter(private val onItemClick: (Product) -> Unit)
    : ListAdapter<Product, ProductAdapter.ViewHolder>(DIFF_UTIL) {

    var originalList: List<Product> = emptyList()
    private var filteredList: MutableList<Product> = mutableListOf()
    companion object {
        private val DIFF_UTIL =
            object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(
                    oldItem: Product,
                    newItem: Product
                ) = oldItem.productName == newItem.productName

                override fun areContentsTheSame(
                    oldItem: Product,
                    newItem: Product
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

        fun bind(productEntity: Product) {
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
    fun filterList(keyword: String) {
        filteredList.clear()
        if (keyword.isNotEmpty()) {
            val searchKeyword = keyword.lowercase()
            originalList.forEach { item ->
                if (item.productName.lowercase().contains(searchKeyword) ||
                    item.productType.lowercase().contains(searchKeyword)
                ) {
                    filteredList.add(item)
                }
            }
        } else {
            filteredList.addAll(originalList)
        }
        submitList(filteredList)
    }
}