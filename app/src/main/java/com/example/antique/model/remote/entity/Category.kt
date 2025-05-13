package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: String = "",
    val cid: Int = -1,
    val name: String,
    val description: String,
    val image: String = ""
) {
    constructor() : this(id = "", cid = -1, name = "", description = "", image = "")
}

