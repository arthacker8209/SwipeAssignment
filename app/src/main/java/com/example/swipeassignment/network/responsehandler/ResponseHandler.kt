package com.example.swipeassignment.network.responsehandler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


suspend fun <T>executeRetrofitApi(call: suspend () -> Response<T>): NetworkResource<T>{
    return withContext(Dispatchers.IO){
        try {
            val result = call()
            when{
                result.isSuccessful -> {
                    val body = result.body()
                    if (body!=null){
                        NetworkResource.Success(body)
                    }
                    else{
                        NetworkResource.Error(
                            ErrorResponse(
                                responseCode = result.code(),
                                retrofitErrorResponse = result.errorBody(),
                                errorStatus = ErrorStatus.ServerError
                            )
                        )
                    }
                }
                else -> {
                    NetworkResource.Error(
                        ErrorResponse(
                            responseCode = result.code(),
                            retrofitErrorResponse = result.errorBody(),
                            errorStatus = ErrorStatus.InvalidError
                        )
                    )
                }

            }
        }catch (throwable: Throwable){
            when(throwable){
                is IOException -> {
                    NetworkResource.Error(
                        ErrorResponse(
                            exception = throwable,
                            errorStatus = ErrorStatus.NetworkError
                        )
                    )
                }
                is HttpException -> {
                    NetworkResource.Error(
                        ErrorResponse(
                            exception = throwable,
                            errorStatus = ErrorStatus.HTTPError
                        )
                    )
                }
                else -> {
                    NetworkResource.Error(
                        ErrorResponse(
                            exception = throwable,
                            errorStatus = ErrorStatus.GotException
                        )
                    )
                }
            }
        }
    }
}