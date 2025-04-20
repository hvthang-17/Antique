package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Coupon(
    var id: String = "",
    val code: String = "",
    val discountPercent: Int,
    val minPrice: Double,
    val expiryDate: Long = 0L
) {
    constructor() : this(
        id = "",
        code = "",
        discountPercent = 0,
        minPrice = 0.0,
        expiryDate = 0L
    )
}
