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


    // creating a collection reference for our Firebase Firestore database.
    val productCollectionRef: CollectionReference = UserRepository.db.collection("product")

  //  val db by lazy { Firebase.firestore }

  //  private val productCollectionRef by lazy { db.collection("product") }

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

    suspend fun getProductByName(productName: String): List<Product> {
        val name = productName.split(" ")
        val products = getAllProducts()
        Log.d("name", name[name.size - 1])
        return products.filter { it.title.contains(name[name.size - 1], true) }
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
        val matchingProducts = emptyList<Product>().toMutableList()

        for (product in products) {
            var avgReviews = 0f
            for (reviews in product.reviews) {
                avgReviews += reviews.stars

                if (avgReviews / (product.reviews.size.toFloat()) >= 3) matchingProducts?.add(
                    product
                )
            }
        }
        return matchingProducts
    }

    suspend fun getTrending(): List<Product> {
        val orders =
            OrderRepository.db.collection("order").get().await().toObjects(Order::class.java)
        val orderItems = orders.map { it.items }.flatten()

        val products = getAllProducts()
        val matchingProducts = mutableListOf<Product>()

        for (product in products) {
            for (orderItem in orderItems) {
                if (product.id == orderItem.pid) {

                    if (orderItem.quantity >= 5) {
                        matchingProducts.add(product)
                        break
                    }
                }
            }
        }
        return matchingProducts
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
        return productCollectionRef.whereGreaterThan("price", startPrice)
            .whereLessThan("price", endPrice).get().await().toObjects(
                Product::class.java
            )
    }
}