package com.example.swipeassignment.network

import com.example.swipeassignment.network.interceptor.NetworkStateChecker
import com.example.swipeassignment.network.interceptor.NetworkStateCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindingModule {
    @Binds
    fun bindNetworkStateChecker(networkStateCheckerImpl: NetworkStateCheckerImpl): NetworkStateChecker
}