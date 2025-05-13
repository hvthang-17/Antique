package com.example.antique.model.repository

import com.example.antique.model.remote.entity.Category
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.tasks.await

object CategoryRepository {
    private val db by lazy { Firebase.firestore }
    private val categoryCollectionRef by lazy { db.collection("category") }

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    suspend fun insertCategory(category: Category) {
        val documentId = categoryCollectionRef.document().id
        category.id = documentId
        categoryCollectionRef.document(documentId).set(category).await()
    }

    suspend fun updateCategory(category: Category) {
        categoryCollectionRef.document(category.id).set(category).await()
    }

    suspend fun deleteCategory(categoryId: String) {
        categoryCollectionRef.document(categoryId).delete().await()
    }

    suspend fun getAllCategories(): List<Category> = categoryCollectionRef.get().await().toObjects(
            Category::class.java
        )

    suspend fun getCategoryById(categoryId: String): Category? {
        val documentSnapshot = categoryCollectionRef.document(categoryId).get().await()
        return if (documentSnapshot.exists()) {
            documentSnapshot.toObject(Category::class.java)
        } else {
            null
        }
    }
}
