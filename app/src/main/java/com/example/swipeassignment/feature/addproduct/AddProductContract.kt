package com.example.swipeassignment.feature.addproduct

import com.example.swipeassignment.network.models.Product
import com.example.swipeassignment.network.responsehandler.NetworkResource
import java.io.File

class AddProductContract {
    interface Repository {
        suspend fun postProduct(
            productName: String?,
            productType: String?,
            price: String?,
            tax: String?,
            imageFiles: List<File>?
        ): NetworkResource<Product.AddProductResponse>
    }
}
