package com.example.swipeassignment.feature.product

import com.example.swipeassignment.network.models.ProductListing
import com.example.swipeassignment.network.responsehandler.NetworkResource
import com.example.swipeassignment.network.responsehandler.executeRetrofitApi
import com.example.swipeassignment.network.service.SwipeApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val swipeApiService: SwipeApiService
): ProductContract.Repository {

    override suspend fun fetchProductListings(): NetworkResource<List<ProductListing>> {
        return executeRetrofitApi {
            swipeApiService.getProductListings()
        }
    }
}