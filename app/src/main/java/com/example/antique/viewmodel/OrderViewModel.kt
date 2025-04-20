package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.repository.OrderRepository
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.OrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class OrderViewModel(val context: Application) : AndroidViewModel(context) {
    private val orderRepository = OrderRepository

    var orders = mutableStateOf<List<Order>>(emptyList())
    var orderStatus = mutableStateOf("Processing")
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

    fun updateStatus(order: Order, status: String){
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.updateOrderStatus(order, status)
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
}