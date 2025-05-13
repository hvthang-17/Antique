package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    var id: String = "",
    val uid: String = "",
    val name: String,
    val street: String = "",
    val ward: String = "",
    val district: String = "",
    val city: String = "",
    val phone: String,
    val poBox: Int,
) {
    constructor() : this(
        id = "",
        uid = "",
        name = "",
        street = "",
        ward = "",
        district = "",
        city = "",
        phone = "",
        poBox = 0
    )
}
