package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.repository.*
import com.example.antique.model.remote.entity.Cart
import com.example.antique.model.remote.entity.Product
import com.example.antique.model.remote.entity.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductViewModel(val context: Application) : AndroidViewModel(context) {
    private val productRepository = ProductRepository
    private val cartRepository = CartRepository
    private val orderRepository = OrderRepository

    private val placeHolderProduct =
        Product(
            cid = "",
            title = "",
            price = 0.0,
            description = "",
            stock = 0,
            image = "",
            timeAdded = 0,
            reviews = mutableListOf<Review>()
        )

    val product = mutableStateOf(placeHolderProduct)
    val products = mutableStateOf<List<Product>>(emptyList())

    fun getProducts() {
        viewModelScope.launch {
            products.value = productRepository.getAllProducts()
        }
    }

    fun setCurrentProduct(productId: String) {
        if (productId != "") {
            var tempProduct = placeHolderProduct
            viewModelScope.launch(Dispatchers.IO) {
                tempProduct = productRepository.getProductById(productId)!!
            }
            viewModelScope.launch(Dispatchers.Main) {
                product.value = tempProduct
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.deleteProduct(productId)
            getProducts()
        }
    }

    fun addToCart(userId: String, productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.insertCart(Cart(uid = userId, pid = productId, quantity = 1))
        }
    }

    fun getProductAvgRating(reviews: MutableList<Review>): Double {
        var productAvgRating = 0.0

        if (reviews.size == 0) return 0.0

        for (review in reviews) productAvgRating += review.stars
        return productAvgRating / reviews.size
    }

    fun userBoughtItem(userId: String, productId: String): Boolean {
        var userBoughtIt = false
        runBlocking {
            this.launch(Dispatchers.IO) {
                userBoughtIt = orderRepository.userBoughtItem(userId, productId)
            }
        }
        return userBoughtIt
    }
}