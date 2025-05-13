package com.example.antique.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.remote.entity.Product
import com.example.antique.model.remote.entity.Review
import com.example.antique.model.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageProductVM(val context: Application) : AndroidViewModel(context) {
    private val productRepository = ProductRepository

    private val placeholderProduct = Product(
        cid = "",
        title = "",
        price = 0.0,
        description = "",
        stock = 0,
        image = "",
        timeAdded = 0,
        reviews = mutableListOf<Review>()
    )
    private val currentProduct = mutableStateOf(placeholderProduct)

    var title by mutableStateOf("")
    var price by mutableStateOf("")
    var description by mutableStateOf("")
    var stock by mutableStateOf("")
    var cid by mutableStateOf("")
    var imageUrl by mutableStateOf("")

    fun setCurrentProduct(product: Product) {
        currentProduct.value = product
        title = product.title
        description = product.description
        price = product.price.toString()
        stock = product.stock.toString()
        cid = product.cid
        imageUrl = product.image
    }

    fun resetCurrentProduct() {
        currentProduct.value = placeholderProduct
        title = ""
        description = ""
        price = ""
        stock = ""
        cid = ""
        imageUrl = ""
    }

    fun addProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.insertProduct(
                Product(
                    id = "",
                    cid = cid,
                    title = title,
                    price = price.toDouble(),
                    description = description,
                    stock = stock.toInt(),
                    image = imageUrl,
                    timeAdded = System.currentTimeMillis(),
                    reviews = mutableListOf()
                )
            )
        }
    }

    fun updateProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.updateProduct(
                currentProduct.value.copy(
                    title = title,
                    description = description,
                    price = price.toDouble(),
                    stock = stock.toInt(),
                    cid = cid,
                    image = imageUrl
                )
            )
        }
    }
}