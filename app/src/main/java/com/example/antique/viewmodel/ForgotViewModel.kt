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
import kotlin.random.Random

class ForgotViewModel(val context: Application) : AndroidViewModel(context) {
    private val userRepository = UserRepository
    private var currentUser: User? = null
    private var verificationCode: Int? = null

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var rePassword by mutableStateOf("")
    var vCode by mutableStateOf("")

    private var error = false

    fun validateEmailInput(): Boolean {
        error = getUserByEmail(email) == null
        return !error
    }

    fun getUserByEmail(email: String): User? {
        runBlocking {
            this.launch(Dispatchers.IO) {
                currentUser = userRepository.getUserByEmail(email)
            }
        }
        return currentUser
    }

    fun generateVCode() {
        verificationCode = Random.nextInt(1000, 10000)

    }

    fun getVCode(): Int? {
        return verificationCode
    }

    fun verify(): Boolean {
        if (vCode == verificationCode.toString())
            return true
        return false
    }

    fun passwordMatch(): Boolean {
        if (password == rePassword) return true
        return false
    }

    fun resetPassword(): Boolean {
        runBlocking {
            this.launch(Dispatchers.IO) {
                var user = currentUser

                if (user != null) {
                    user.password = password
                }

                if (user != null) {
                    userRepository.updateUser(user)
                }
            }
        }
        return getUserByEmail(email)?.password == password
    }
}