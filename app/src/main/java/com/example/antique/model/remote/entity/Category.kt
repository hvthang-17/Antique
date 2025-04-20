package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: String = "",
    val cid: Int = -1,
    val name: String
) {
    constructor() : this(id = "", cid = -1, name = "")
}

