package com.example.swipeassignment.network.responsehandler

sealed class NetworkResource<out T>{

    data class Success<out T>(val data: T) : NetworkResource<T>()
    data class Error<out T>(
        val errorResponse: ErrorResponse
    ):NetworkResource<T>()
}
