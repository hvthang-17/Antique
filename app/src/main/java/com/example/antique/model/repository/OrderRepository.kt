package com.example.antique.model.repository

import android.util.Log
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestoreSettings
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.OrderItem
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

object OrderRepository {
    private val TAG = "OrderRepo"

    val db by lazy { Firebase.firestore }
    private val orderCollectionRef by lazy { db.collection("order") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getAllOrders(): List<Order> = orderCollectionRef.get().await().toObjects(
        Order::class.java
    )

    suspend fun updateOrderStatus(order: Order, status: String) {
        order.status = status
        orderCollectionRef.document(order.id).set(order)
    }

    suspend fun getAllOrderItems(): List<OrderItem> {
        val orders = orderCollectionRef.get().await().toObjects(Order::class.java)
        return orders.map { it.items }.flatten()
    }

    suspend fun getOrderById(order_id: String): Order? =
        orderCollectionRef.document(order_id).get().await().toObject(
            Order::class.java
        )

    suspend fun getOrderByUserId(userId: String): List<Order> =
        orderCollectionRef.whereEqualTo("uid", userId).get().await().toObjects(
            Order::class.java
        )

    fun insertOrder(order: Order): String {
        runBlocking {
            var documentId: String
            var isUnique = false

            while (!isUnique) {
                documentId = "ORD" + (1000..9999).random()

                val existingOrder = orderCollectionRef.document(documentId).get().await()
                if (!existingOrder.exists()) {
                    isUnique = true
                    order.id = documentId
                    orderCollectionRef.document(documentId).set(order)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot written with ID: $documentId")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                    return@runBlocking documentId
                }
            }
        }

        return ""
    }


    suspend fun getFilteredOrders(status: String): List<Order> {
        return orderCollectionRef.whereEqualTo("status", status)
            .get().await()
            .toObjects(Order::class.java)
    }

    suspend fun userBoughtItem(userId: String, productId: String): Boolean {
        val userOrders = orderCollectionRef.whereEqualTo("uid", userId)
            .get().await()
            .toObjects(Order::class.java)

        for (order in userOrders) {
            if (order.status == "Đã giao") {
                for (item in order.items) {
                    if (item.pid == productId) return true
                }
            }
        }
        return false
    }
}