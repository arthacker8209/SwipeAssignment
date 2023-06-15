package com.example.swipeassignment.common

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Error(val errorMessage: String) : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
}
