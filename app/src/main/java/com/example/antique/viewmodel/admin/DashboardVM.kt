package com.example.antique.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.antique.model.remote.entity.Category
import com.example.antique.model.remote.entity.Coupon
import com.example.antique.model.repository.ProductRepository
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.Product
import com.example.antique.model.repository.CategoryRepository
import com.example.antique.model.repository.CouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DashboardVM(val context: Application) : AndroidViewModel(context) {
    private val productRepo = ProductRepository
    private val categoryRepo = CategoryRepository
    private val couponRepo = CouponRepository

    var selectedSortChip by mutableStateOf(0)
    var allOrders =  mutableListOf<Order>()
    var products  =  mutableListOf<Product>()
    var categories  =  mutableListOf<Category>()
    var coupons  =  mutableListOf<Coupon>()

    fun getAllProducts(): List<Product> {
        var products = emptyList<Product>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                products = productRepo.getAllProducts()
            }
        }
        return products
    }

    fun getAllCategories(): List<Category> {
        var categories = emptyList<Category>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                categories = categoryRepo.getAllCategories()
            }
        }
        return categories
    }

    fun getAllCoupons(): List<Coupon> {
        var coupons = emptyList<Coupon>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                coupons = couponRepo.getAllCoupons()
            }
        }
        return coupons
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

    fun getLatestOrders(): List<Order> {
        when (selectedSortChip) {
            1 -> allOrders.sortBy { it.status }
            2 -> allOrders.sortBy { it.date }
            3 ->  allOrders.sortByDescending { it.total }
            else -> allOrders.sortBy { it.id }
        }
        return allOrders
    }

    fun getMonthlySales(): List<Double> {
        val monthlySales = MutableList(12) { 0.0 }

        for (order in allOrders) {
            try {
                val parts = order.date.split("/")
                if (parts.size >= 2) {
                    val month = parts[0].toIntOrNull() ?: continue
                    val index = month - 1
                    if (index in 0..11) {
                        monthlySales[index] += order.total
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return monthlySales
    }


}