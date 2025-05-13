package com.example.antique.model.repository

import com.example.antique.model.remote.entity.Coupon
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object CouponRepository {
    private val db = Firebase.firestore
    private val couponCollection = db.collection("coupon")

    suspend fun insertCoupon(coupon: Coupon) {
        val id = couponCollection.document().id
        coupon.id = id
        couponCollection.document(id).set(coupon).await()
    }

    suspend fun updateCoupon(coupon: Coupon) {
        if (coupon.id.isNotBlank()) {
            couponCollection.document(coupon.id).set(coupon).await()
        }
    }

    suspend fun deleteCoupon(couponId: String) {
        if (couponId.isNotBlank()) {
            couponCollection.document(couponId).delete().await()
        }
    }

    suspend fun getAllCoupons(): List<Coupon> = couponCollection.get().await().toObjects(
        Coupon::class.java
    )

    suspend fun getCouponById(couponId: String): Coupon? =
        couponCollection.document(couponId).get().await().toObject(
            Coupon::class.java
        )
}
