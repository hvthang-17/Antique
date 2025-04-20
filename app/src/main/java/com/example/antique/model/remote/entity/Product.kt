package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    var id: String = "",
    val cid: String,
    val title: String,
    val price: Double,
    val description: String,
    val stock: Int,
    val image: String,
    val timeAdded: Long,
    var reviews: MutableList<Review>
) {
    constructor() : this(
        id = "",
        cid = "",
        title = "",
        price = 0.0,
        description = "",
        stock = 0,
        image = "",
        timeAdded = 0,
        reviews = mutableListOf<Review>()
    )
}
