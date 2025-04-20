package com.example.antique.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.repository.ProductRepository
import com.example.antique.model.remote.entity.Product
import com.example.antique.model.remote.entity.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReviewViewModel(val context: Application) : AndroidViewModel(context) {
    private val productRepository = ProductRepository

    private var currentUserId: String = ""
    private var productId: String = ""

    var reviewTitle by mutableStateOf("")
    var reviewDescription by mutableStateOf("")
    var ratingValue by mutableStateOf(0)

    var titleError = false
    var descriptionError = false
    var ratingError = false

    private var error = false

    var openDialog by mutableStateOf(false)

    var review: Review? = null
    private fun getReviewByUserAndProduct(): Review? {
        viewModelScope.launch(Dispatchers.IO) {
            review = productRepository.getReviewByUserAndProduct(currentUserId, productId)
        }
        viewModelScope.launch(Dispatchers.Main) {
            reviewTitle = review?.title ?: ""
            reviewDescription = review?.details ?: ""
            ratingValue = review?.stars ?: 0
        }
        return review
    }

    fun returnReview(): Review? {
        return review
    }

    fun getProduct(): Product? {
        var product: Product? = null
        runBlocking {
            this.launch(Dispatchers.IO) {
                product = productRepository.getProductById(productId)
            }
        }
        return product
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        return current.format(formatter)
    }

    fun deleteReview() = viewModelScope.launch(Dispatchers.IO) {
        val review = getReviewByUserAndProduct()
        review?.let { productRepository.deleteReview(productId, review.uid) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateReview() = viewModelScope.launch(Dispatchers.IO) {
        productRepository.updateReview(
            productId,
            Review(
                currentUserId,
                reviewTitle,
                reviewDescription,
                ratingValue,
                getCurrentDate()
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addReview() = viewModelScope.launch(Dispatchers.IO) {
        val review = Review(
            uid = currentUserId,
            title = reviewTitle,
            details = reviewDescription,
            stars = ratingValue,
            date = getCurrentDate()
        )
        productRepository.insertReview(productId, review)
    }

    fun validateReviewInput(): Boolean {
        if (reviewTitle.isEmpty()) titleError = true
        if (reviewDescription.isEmpty()) descriptionError = true
        if (ratingValue == 0) ratingError = true

        error = titleError || descriptionError || ratingError
        return !error
    }

    fun setUserId(userId: String) {
        currentUserId = userId
    }

    fun setProductId(product_id: String) {
        productId = product_id
        getReviewByUserAndProduct()
    }
}