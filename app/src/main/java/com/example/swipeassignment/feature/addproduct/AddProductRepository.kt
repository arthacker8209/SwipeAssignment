package com.example.swipeassignment.feature.addproduct

import com.example.swipeassignment.network.models.Product
import com.example.swipeassignment.network.responsehandler.NetworkResource
import com.example.swipeassignment.network.responsehandler.executeRetrofitApi
import com.example.swipeassignment.network.service.SwipeApiService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AddProductRepository @Inject constructor(
    private val swipeApiService: SwipeApiService
) : AddProductContract.Repository {

    override suspend fun postProduct(
        productName: String?,
        productType: String?,
        price: String?,
        tax: String?,
        imageFiles: List<File>?
    ): NetworkResource<Product.AddProductResponse> {
        val imageParts: List<MultipartBody.Part> = imageFiles?.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("files[]", file.name, requestFile)
        } ?: listOf<MultipartBody.Part>()
        val productNameRequest = productName?.toRequestBody("text/plain".toMediaTypeOrNull())
        val productTypeRequest = productType?.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceRequest = price?.toRequestBody("text/plain".toMediaTypeOrNull())
        val taxRequest = tax?.toRequestBody("text/plain".toMediaTypeOrNull())
        return executeRetrofitApi {
            swipeApiService.addProduct(
                productName = productNameRequest,
                productType = productTypeRequest,
                price = priceRequest,
                tax = taxRequest,
                files = imageParts
            )
        }
    }
}