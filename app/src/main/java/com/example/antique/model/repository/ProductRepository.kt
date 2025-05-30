package com.example.antique.model.repository

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.Product
import com.example.antique.model.remote.entity.Review
import kotlinx.coroutines.tasks.await

object ProductRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val productCollectionRef: CollectionReference = UserRepository.db.collection("product")

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getAllProducts(): List<Product> = productCollectionRef.get().await().toObjects(
        Product::class.java
    )

    suspend fun getProductsByCategory(category: String): List<Product> {
        return productCollectionRef.whereEqualTo("cid", category).get().await().toObjects(
            Product::class.java
        )
    }

    fun removeVietnameseDiacritics(str: String): String {
        val vietnameseMap = mapOf(
            'à' to 'a', 'á' to 'a', 'ạ' to 'a', 'ả' to 'a', 'ã' to 'a',
            'â' to 'a', 'ầ' to 'a', 'ấ' to 'a', 'ậ' to 'a', 'ẩ' to 'a', 'ẫ' to 'a',
            'ă' to 'a', 'ằ' to 'a', 'ắ' to 'a', 'ặ' to 'a', 'ẳ' to 'a', 'ẵ' to 'a',
            'è' to 'e', 'é' to 'e', 'ẹ' to 'e', 'ẻ' to 'e', 'ẽ' to 'e',
            'ê' to 'e', 'ề' to 'e', 'ế' to 'e', 'ệ' to 'e', 'ể' to 'e', 'ễ' to 'e',
            'ì' to 'i', 'í' to 'i', 'ị' to 'i', 'ỉ' to 'i', 'ĩ' to 'i',
            'ò' to 'o', 'ó' to 'o', 'ọ' to 'o', 'ỏ' to 'o', 'õ' to 'o',
            'ô' to 'o', 'ồ' to 'o', 'ố' to 'o', 'ộ' to 'o', 'ổ' to 'o', 'ỗ' to 'o',
            'ơ' to 'o', 'ờ' to 'o', 'ớ' to 'o', 'ợ' to 'o', 'ở' to 'o', 'ỡ' to 'o',
            'ù' to 'u', 'ú' to 'u', 'ụ' to 'u', 'ủ' to 'u', 'ũ' to 'u',
            'ư' to 'u', 'ừ' to 'u', 'ứ' to 'u', 'ự' to 'u', 'ử' to 'u', 'ữ' to 'u',
            'ỳ' to 'y', 'ý' to 'y', 'ỵ' to 'y', 'ỷ' to 'y', 'ỹ' to 'y',
            'đ' to 'd'
        )
        return str.map { vietnameseMap[it] ?: it }.joinToString("")
    }

    suspend fun getProductByName(productName: String): List<Product> {
        val cleanSearch = removeVietnameseDiacritics(productName.trim().lowercase())
        val products = getAllProducts()
        return products.filter { product ->
            val titleNormalized = removeVietnameseDiacritics(product.title.lowercase())
            titleNormalized.contains(cleanSearch)
        }
    }


    suspend fun getProductById(productId: String): Product? =
        productCollectionRef.document(productId).get().await().toObject(
            Product::class.java
        )

    suspend fun updateProduct(product: Product) {
        productCollectionRef.document(product.id).set(product).await()
    }

    fun insertProduct(product: Product) {
        val documentId = productCollectionRef.document().id
        product.id = documentId
        productCollectionRef.document(documentId).set(product)
    }

    suspend fun insertReview(productId: String, review: Review) {
        val product = getProductById(productId)
        product?.let {
            product.reviews.add(review)
            productCollectionRef.document(productId).set(product).await()
        }
    }

    suspend fun deleteReview(productId: String, userId: String) {
        val product = getProductById(productId)
        product?.let {
            val reviews = mutableListOf<Review>()
            for (review in product.reviews) if (review.uid != userId) reviews.add(review)

            product.reviews = reviews
            productCollectionRef.document(productId).set(product).await()
        }
    }

    suspend fun updateReview(productId: String, updatedReview: Review) {
        val product = getProductById(productId)
        product?.let {
            val reviews = mutableListOf<Review>()
            for (review in product.reviews) if (review.uid != updatedReview.uid) reviews.add(review)
            reviews.add(updatedReview)

            product.reviews = reviews
            productCollectionRef.document(productId).set(product).await()
        }
    }

    suspend fun getReviewByUserAndProduct(userId: String, productId: String): Review? {
        val product = getProductById(productId)
        product?.let {
            for (review in product.reviews) if (review.uid == userId) return review
        }
        return null
    }

    suspend fun deleteProduct(productId: String) {
        productCollectionRef.document(productId).delete().await()
    }

    suspend fun getNewArrivals(): List<Product> =
        productCollectionRef.orderBy("timeAdded", Query.Direction.DESCENDING).get().await()
            .toObjects(
                Product::class.java
            )

    suspend fun getTopRanked(): List<Product> {
        val products = getAllProducts()
        val matchingProducts = mutableListOf<Product>()

        for (product in products) {
            val totalStars = product.reviews.sumOf { it.stars }
            val avgStars = if (product.reviews.isNotEmpty()) totalStars / product.reviews.size.toFloat() else 0f

            if (avgStars >= 4f) {
                matchingProducts.add(product)
            }
        }

        return matchingProducts
    }


    suspend fun getTrending(): List<Product> {
        val orders = OrderRepository.db
            .collection("order")
            .get()
            .await()
            .toObjects(Order::class.java)

        val orderItems = orders.flatMap { it.items }
        val products = getAllProducts()

        return products.filter { product ->
            orderItems.any { it.pid == product.id && it.quantity >= 5 }
        }
    }

    suspend fun getCategoryProducts(
        cid: String, startPrice: Float, endPrice: Float
    ): List<Product>? {
        return productCollectionRef.whereEqualTo("cid", cid).whereGreaterThan("price", startPrice)
            .whereLessThan("price", endPrice).get().await().toObjects(
                Product::class.java
            )
    }

    suspend fun getNormalFilterQuery(
        startPrice: Float, endPrice: Float
    ): List<Product>? {
        return productCollectionRef
            .whereGreaterThan("price", startPrice)
            .whereLessThan("price", endPrice)
            .get()
            .await()
            .toObjects(Product::class.java)
    }


    suspend fun updateProductStock(productId: String, newStock: Int) {
        try {
            productCollectionRef.document(productId).update("stock", newStock).await()
            Log.d("ProductRepository", "Stock updated for $productId to $newStock")
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error updating stock for $productId: ${e.message}")
        }
    }

}