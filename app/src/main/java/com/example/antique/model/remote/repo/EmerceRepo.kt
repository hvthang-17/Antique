package com.example.antique.model.remote.repo

import com.example.antique.model.remote.api.EmerceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EmerceRepo {
    private const val BASE_URL =
        "https://gist.githubusercontent.com/hani-hj1908619/437ddd8aa6fb9c2c7dd5643f5f3a72aa/raw/0074162f7995f2897503c194dfb451ad5b2d798a/"
    private val emerceApi: EmerceApi by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(EmerceApi::class.java)
    }

    suspend fun getUsers() = emerceApi.getUsers()
    suspend fun getCategories() = emerceApi.getCategories()
    suspend fun getProducts() = emerceApi.getProducts()
}