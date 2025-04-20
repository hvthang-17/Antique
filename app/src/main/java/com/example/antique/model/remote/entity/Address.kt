package com.example.antique.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    var id: String = "",
    val uid: String = "",   //ID người dùng
    val name: String,   //TenKH
    val street: String = "",   //TenDuong
    val ward: String = "",   // Phường/Xã
    val district: String = "",   // Quận/Huyện
    val city: String = "",     // Tỉnh/Thành phố
    val phone: String,
    val poBox: Int,   //Mã bưu điện
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
