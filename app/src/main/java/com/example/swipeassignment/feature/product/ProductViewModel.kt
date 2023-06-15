package com.example.swipeassignment.feature.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.common.CoroutinesDispatcherProvider
import com.example.swipeassignment.common.ViewState
import com.example.swipeassignment.network.models.ProductListing
import com.example.swipeassignment.network.responsehandler.NetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    private val repository: ProductContract.Repository
) : ViewModel() {

    private val _productLiveData = MutableLiveData<ViewState<List<ProductListing>>>()
    val productLiveData: LiveData<ViewState<List<ProductListing>>> get() = _productLiveData

    fun fetchProducts(){
        _productLiveData.value = ViewState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            when(val result = repository.fetchProductListings()){
                is NetworkResource.Error -> {
                    _productLiveData.value = ViewState.Error(result.errorResponse.retrofitErrorResponse.toString())
                }
                is NetworkResource.Success -> {
                    withContext(dispatcherProvider.main){
                        val productListing = result.data
                        _productLiveData.value = ViewState.Success(productListing)
                    }
                }
            }
        }
    }
}