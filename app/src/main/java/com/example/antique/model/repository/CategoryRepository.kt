package com.example.antique.model.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestoreSettings
import com.example.antique.model.remote.entity.Category
import kotlinx.coroutines.tasks.await

object CategoryRepository {
    val db by lazy { Firebase.firestore }
    private val categoryCollectionRef by lazy { db.collection("category") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    fun insertCategory(category: Category) {
        val documentId = categoryCollectionRef.document().id
        category.id = documentId
        categoryCollectionRef.document(documentId).set(category)
    }

    suspend fun getAllCategories(): List<Category> = categoryCollectionRef.orderBy("cid", Query.Direction.ASCENDING).get().await().toObjects(
        Category::class.java
    )
}