package com.example.swipeassignment.feature.addproduct

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AddProductModule {
    @Binds
    fun bindAddProductRepository(addProductRepository: AddProductRepository): AddProductContract.Repository
}