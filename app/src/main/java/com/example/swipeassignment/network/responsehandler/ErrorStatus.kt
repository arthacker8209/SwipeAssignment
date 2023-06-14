package com.example.swipeassignment.network.responsehandler

sealed class ErrorStatus {
    object HTTPError : ErrorStatus()
    object NetworkError : ErrorStatus()
    object GotException : ErrorStatus()
    object InvalidError : ErrorStatus()
    object ServerError : ErrorStatus()
}