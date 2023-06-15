package com.example.swipeassignment.network.service

import com.example.swipeassignment.network.models.ProductListing
import retrofit2.Response
import retrofit2.http.GET

interface SwipeApiService {
    @GET("api/public/get")
    suspend fun getProductListings(): Response<List<ProductListing>>


}
