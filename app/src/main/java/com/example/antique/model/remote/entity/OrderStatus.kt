package com.example.antique.model.remote.entity

@kotlinx.serialization.Serializable
enum class OrderStatus(val code: String)  {
    All("Tất cả"),
    Processing("Đang xử lý"),
    Shipped("Đang giao"),
    Delivered("Đã giao")
}
