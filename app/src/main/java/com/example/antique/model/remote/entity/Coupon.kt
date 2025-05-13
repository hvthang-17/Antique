package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Coupon(
    var id: String = "",
    val code: String = "",
    val discountPercent: Int,
    val expiryDate: String
) {
    constructor() : this(
        id = "",
        code = "",
        discountPercent = 0,
        expiryDate = ""
    )
}
