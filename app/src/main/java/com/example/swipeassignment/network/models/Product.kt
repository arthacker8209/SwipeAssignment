package com.example.swipeassignment.network.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("image") var image: String = "",
    @SerializedName("price") var price: Double? = null,
    @SerializedName("product_name") var productName: String = "",
    @SerializedName("product_type") var productType: String = "",
    @SerializedName("tax") var tax: Double? = null
){
    data class AddProductResponse(
        @SerializedName("message") val message: String,
        @SerializedName("product_details") val productDetails: Product,
        @SerializedName("product_id") val productId: Int,
        @SerializedName("success") val success: Boolean
    )
}
