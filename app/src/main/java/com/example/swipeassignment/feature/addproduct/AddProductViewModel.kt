package com.example.swipeassignment.feature.addproduct

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.common.CoroutinesDispatcherProvider
import com.example.swipeassignment.common.ViewState
import com.example.swipeassignment.feature.product.ProductContract
import com.example.swipeassignment.network.models.Product
import com.example.swipeassignment.network.responsehandler.NetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    private val repository: AddProductContract.Repository
) : ViewModel() {
    private val _addProductLiveData = MutableLiveData<ViewState<Product.AddProductResponse>>()
    val productLiveData: LiveData<ViewState<Product.AddProductResponse>> get() = _addProductLiveData
    val imageFilesLiveData = MutableLiveData<List<File>?>()
    val errorMessageLiveData = MutableLiveData<String>()
    fun addProduct(
        productName: String?,
        productType: String?,
        price: String?,
        tax: String?,
        imageFiles: List<File>?
    ) {
        _addProductLiveData.value = ViewState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result =
                repository.postProduct(productName, productType, price, tax, imageFiles)) {
                is NetworkResource.Error -> {
                    withContext(dispatcherProvider.main) {
                        _addProductLiveData.value = ViewState.Error(result.errorResponse.toString())
                    }
                }

                is NetworkResource.Success -> {
                    withContext(dispatcherProvider.main) {
                        _addProductLiveData.value = ViewState.Success(result.data)
                    }
                }
            }
        }

    }
    fun onSubmitClicked(
        productName: String,
        productType: String,
        price: String,
        tax: String
    ) {
        var imageFiles = imageFilesLiveData.value

        if (validateFields(productName, productType, price, tax)) {
            if (imageFiles.isNullOrEmpty()) {
                imageFiles = listOf<File>()
            }
            addProduct(productName, productType, price, tax, imageFiles)
        } else {
            errorMessageLiveData.value = "Complete all fields first"
        }
    }
    private fun validateFields(
        productName: String?,
        productType: String?,
        price: String?,
        tax: String?,
    ): Boolean {

        if (productName.isNullOrEmpty()) {
            return false
        }

        if (productType.isNullOrEmpty()) {
            return false
        }

        if (price.isNullOrEmpty()) {
            return false
        }
        if (tax.isNullOrEmpty()) {
            return false
        }
        return true
    }
}