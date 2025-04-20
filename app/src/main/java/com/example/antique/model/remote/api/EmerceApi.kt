package com.example.antique.model.remote.api

import com.example.antique.model.remote.entity.*
import retrofit2.http.*

interface EmerceApi {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("products")
    suspend fun getProducts(): List<Product>
}
