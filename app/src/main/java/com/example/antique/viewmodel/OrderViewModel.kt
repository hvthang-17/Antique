package com.example.antique.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.remote.entity.Content
import com.example.antique.model.remote.entity.Email
import com.example.antique.model.repository.OrderRepository
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.OrderItem
import com.example.antique.model.remote.entity.OrderStatus
import com.example.antique.model.remote.entity.Personalization
import com.example.antique.model.remote.entity.SendGridMailBody
import com.example.antique.model.repository.UserRepository
import com.example.antique.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class OrderViewModel(val context: Application) : AndroidViewModel(context) {
    private val orderRepository = OrderRepository

    var orders = mutableStateOf<List<Order>>(emptyList())
    var orderStatus = mutableStateOf(OrderStatus.Processing)
    var isOrderUpdated = mutableStateOf(false)

    private fun getAllUserOrders() {
        viewModelScope.launch {
            orders.value = orderRepository.getOrderByUserId(currentUserId)
        }
    }

    fun getAllOrders(): List<Order> {
        var orders = emptyList<Order>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                orders = orderRepository.getAllOrders()
            }
        }
        return orders
    }

    fun updateStatus(order: Order, status: OrderStatus){
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.updateOrderStatus(order, status.code)
        }
    }

    fun getAllOrderItems(): List<OrderItem> {
        var orderItems = emptyList<OrderItem>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                orderItems = orderRepository.getAllOrderItems()
            }}
        return orderItems
    }

    private var currentUserId: String = ""

    fun setUserId(user_id: String) {
        currentUserId = user_id
        getAllUserOrders()
    }

    // Lấy user theo uid
    suspend fun getUserById(uid: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val user = UserRepository.getUserById(uid)
                user?.email
            } catch (e: Exception) {
                Log.e("Email", "Lỗi lấy user: ${e.message}")
                null
            }
        }
    }

    fun sendStatusUpdateEmail(userEmail: String, orderStatus: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val body = SendGridMailBody(
                    personalizations = listOf(
                        Personalization(to = listOf(Email(userEmail)))
                    ),
                    from = Email("hoangthang050517@gmail.com"),
                    content = listOf(
                        Content(value = "Trạng thái đơn hàng của bạn đã được cập nhật: $orderStatus")
                    )
                )

                val response = ApiClient.orderApi.sendOrderStatusEmail(body)
                if (response.isSuccessful) {
                    Log.d("SendGrid", "Email đã gửi thành công")
                } else {
                    Log.e("SendGrid", "Lỗi gửi email: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SendGrid", "Lỗi khi gửi email: ${e.message}")
            }
        }
    }
}