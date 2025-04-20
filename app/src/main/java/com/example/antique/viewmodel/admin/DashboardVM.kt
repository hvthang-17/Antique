package com.example.antique.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.antique.model.repository.ProductRepository
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DashboardVM(val context: Application) : AndroidViewModel(context) {
    private val productRepo = ProductRepository

    var selectedSortChip by mutableStateOf(0)
    var allOrders =  mutableListOf<Order>()
    var products  =  mutableListOf<Product>()

    fun getAllProducts(): List<Product> {
        var products = emptyList<Product>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                products = productRepo.getAllProducts()
            }
        }
        return products
    }

    fun getTotalSales() : Double{
       var total = 0.0
        runBlocking {
            this.launch(Dispatchers.IO) {
                for (order in allOrders) {
                    total += order.total
                }
            }
        }
        return total
    }

    fun getLatestOrders(): List<Order> { // List<Order
        when (selectedSortChip) {
            1 -> allOrders.sortBy { it.status }
            2 -> allOrders.sortBy { it.date }
            3 ->  allOrders.sortByDescending { it.total }
            else -> allOrders.sortBy { it.id }
        }
        return allOrders
    }
}