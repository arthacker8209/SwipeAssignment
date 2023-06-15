package com.example.swipeassignment.feature.product

import com.example.swipeassignment.network.models.ProductListing
import com.example.swipeassignment.network.responsehandler.NetworkResource

class ProductContract
 {
   interface Repository {
      suspend fun fetchProductListings(): NetworkResource<List<ProductListing>>
   }
}
