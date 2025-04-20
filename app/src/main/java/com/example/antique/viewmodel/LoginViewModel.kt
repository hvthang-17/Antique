package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.antique.model.remote.entity.User
import com.example.antique.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(val context: Application) : AndroidViewModel(context) {
    private var currentUser: User? = null

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)

    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    private var error = false

    fun validateLoginInput(): Boolean {
        if (email.isEmpty()) emailError = true
        if (password.isEmpty()) passwordError = true

        error = emailError || passwordError
        return !error
    }

    fun authenticateLogin(): Boolean {
        runBlocking {
            this.launch(Dispatchers.IO) {
                currentUser = UserRepository.getUserByEmail(email)
            }
        }
        return password == currentUser?.password
    }

    fun getCurrentUserId(): String {
        return currentUser?.id ?: ""
    }
}