package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    var id: String = "",
    val uid: String = "",
    val pid: String = "",
    var quantity: Int = 1,
) {
    constructor() : this(id = "", uid = "", pid = "", quantity = 1)
}
