package com.example.swipeassignment.network.service

import com.example.swipeassignment.network.models.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SwipeApiService {
    @GET("api/public/get")
    suspend fun getProductListings(): Response<List<Product>>
    @Multipart
    @POST("api/public/add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody?,
        @Part("product_type") productType: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("tax") tax: RequestBody?,
        @Part files: List<MultipartBody.Part>
    ): Response<Product.AddProductResponse>

}
