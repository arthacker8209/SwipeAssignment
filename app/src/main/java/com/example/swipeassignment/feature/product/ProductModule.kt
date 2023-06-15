package com.example.swipeassignment.feature.product

import com.example.swipeassignment.databinding.ActivityMainBinding
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface ProductModule {

    @Binds
    fun bindProductRepository(productRepository: ProductRepository): ProductContract.Repository
}