package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    var id: String = "",
    val uid: String = "",
    val date: String,
    val total: Double,
    val aid: String = "",
    var status: String,
    var items: List<OrderItem>
) {
    constructor() : this(
        id = "", uid = "", date = "", total = 0.0, aid = "", status = "", items = emptyList()
    )
}
