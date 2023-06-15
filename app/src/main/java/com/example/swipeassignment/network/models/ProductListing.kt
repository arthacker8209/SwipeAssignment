package com.example.swipeassignment.network.models

import com.google.gson.annotations.SerializedName

data class ProductListing(
    @SerializedName("image") var image: String = "",
    //@SerializedName("price") var price: Double? = null,
    @SerializedName("product_name") var productName: String = "",
    @SerializedName("product_type") var productType: String = "",
    //@SerializedName("tax") var tax: Double? = null
)
