package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.remote.entity.Coupon
import com.example.antique.model.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CouponViewModel(val context: Application) : AndroidViewModel(context) {
    private val couponRepository = CouponRepository

    private val placeHolderCoupon =
        Coupon (
            code = "",
            discountPercent = 0,
            expiryDate = ""
        )

    val coupon = mutableStateOf(placeHolderCoupon)
    val coupons = mutableStateOf<List<Coupon>>(emptyList())

    fun getCoupons() {
        viewModelScope.launch {
            coupons.value = couponRepository.getAllCoupons()
        }
    }

    fun setCurrentCoupon(couponId: String) {
        if (couponId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val tempCoupon = couponRepository.getCouponById(couponId)
                tempCoupon?.let {
                    coupon.value = it
                }
            }
        }
    }

    fun deleteCoupon(couponId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            couponRepository.deleteCoupon(couponId)
            getCoupons()
        }
    }
}