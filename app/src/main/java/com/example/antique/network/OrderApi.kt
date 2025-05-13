package com.example.antique.network

import com.example.antique.model.remote.entity.SendGridMailBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OrderApi {
    @Headers(
        "Authorization: Bearer AntiqueApp",
        "Content-Type: application/json"
    )
    @POST("v3/mail/send")
    suspend fun sendOrderStatusEmail(@Body body: SendGridMailBody): Response<ResponseBody>
}
