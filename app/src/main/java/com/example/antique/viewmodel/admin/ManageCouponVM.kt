package com.example.antique.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.remote.entity.Coupon
import com.example.antique.model.repository.CouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageCouponVM(val context: Application) : AndroidViewModel(context) {

    private val couponRepository = CouponRepository

    private val placeholderCoupon = Coupon(
        code = "",
        discountPercent = 0,
        expiryDate = ""
    )
    private val currentCoupon = mutableStateOf(placeholderCoupon)

    var code by mutableStateOf("")
    var discountPercent by mutableStateOf(0)
    var expiryDate by mutableStateOf("")
    var expiryDateError by mutableStateOf(false)

    fun setCurrentCoupon(coupon: Coupon) {
        currentCoupon.value = coupon
        code = coupon.code
        discountPercent = coupon.discountPercent
        expiryDate = coupon.expiryDate
    }

    fun resetCurrentCoupon() {
        currentCoupon.value = placeholderCoupon
        code = ""
        discountPercent = 0
        expiryDate = ""
        expiryDateError = false
    }

    fun addCoupon() {
        viewModelScope.launch(Dispatchers.IO) {
            couponRepository.insertCoupon(
                Coupon (
                    id = "",
                    code = code,
                    discountPercent = discountPercent,
                    expiryDate = expiryDate
                )
            )
        }
    }

    fun updateCoupon() {
        viewModelScope.launch(Dispatchers.IO) {
            couponRepository.updateCoupon(
                currentCoupon.value.copy(
                    code = code,
                    discountPercent = discountPercent,
                    expiryDate = expiryDate
                )
            )
        }
    }
}
