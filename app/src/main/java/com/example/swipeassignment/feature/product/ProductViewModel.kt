package com.example.swipeassignment.feature.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.common.CoroutinesDispatcherProvider
import com.example.swipeassignment.common.ViewState
import com.example.swipeassignment.network.models.Product
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

    private val _productLiveData = MutableLiveData<ViewState<List<Product>>>()
    val productLiveData: LiveData<ViewState<List<Product>>> get() = _productLiveData

    fun fetchProducts(){
        _productLiveData.value = ViewState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            when(val result = repository.fetchProductListings()){
                is NetworkResource.Error -> {
                    withContext(dispatcherProvider.main){
                        Log.d("DEEPAK",result.errorResponse.toString())
                        _productLiveData.value = ViewState.Error(result.errorResponse.toString())
                    }
                }
                is NetworkResource.Success -> {
                    withContext(dispatcherProvider.main){
                        Log.d("DEEPAK",result.data.toString())
                        _productLiveData.value = ViewState.Success(result.data)
                    }
                }
            }
        }
    }
}