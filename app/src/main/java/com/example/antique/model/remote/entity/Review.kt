package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val uid: String = "",
    val title: String,
    val details: String = "",
    val stars: Int,
    val date: String
) {
    constructor() : this(
        uid = "",
        title = "",
        details = "",
        stars = 0,
        date = ""
    )
}
