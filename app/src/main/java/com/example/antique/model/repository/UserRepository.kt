package com.example.antique.model.repository

import com.example.antique.model.remote.entity.User
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings

object UserRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val userCollectionRef: CollectionReference = db.collection("user")

    init {
        val settings = firestoreSettings { var isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getUserById(userId: String): User? =
        userCollectionRef.document(userId).get().await().toObject(User::class.java)

    suspend fun getUserByEmail(email: String): User? {
        val users =
            userCollectionRef.whereEqualTo("email", email).get().await().toObjects(User::class.java)
        return if (users.isEmpty()) null
        else users[0]
    }

    fun updateUser(user: User) {
        userCollectionRef.document(user.id).set(user)
    }

    fun insertUser(user: User) {
        val documentId = userCollectionRef.document().id
        user.id = documentId
        userCollectionRef.document(documentId).set(user)
    }
}



