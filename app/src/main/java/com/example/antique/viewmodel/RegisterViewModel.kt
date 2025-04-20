package com.example.antique.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.remote.entity.User
import com.example.antique.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class RegisterViewModel(val context: Application) : AndroidViewModel(context) {
    private val userRepository = UserRepository

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var birthDate = mutableStateOf("")
    var termsIsChecked = mutableStateOf(false)

    var fNameError by mutableStateOf(false)
    var lNameError by mutableStateOf(false)
    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var dobError by mutableStateOf(false)
    var termsError by mutableStateOf(false)

    private var error = false

    fun validateRegisterInput(): Boolean {
        if (firstName.isEmpty()) fNameError = true
        if (lastName.isEmpty()) lNameError = true
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) emailError = true
        if (password.isEmpty()) passwordError = true
        if (birthDate.value.isEmpty()) dobError = true
        if (!termsIsChecked.value) termsError = true

        error = fNameError || lNameError || emailError || passwordError || dobError || termsError
        return !error
    }

    private var dbUser: User? = null
    fun validateExistingAccount(): Boolean {
        runBlocking {
            this.launch(Dispatchers.IO) {
                dbUser = userRepository.getUserByEmail(email)
            }
        }
        dbUser ?: return true
        return false
    }

    fun addUser() = viewModelScope.launch(Dispatchers.IO) {
        val user = User(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            dob = birthDate.value,
            isAdmin = false,
            type = "New"
        )
        userRepository.insertUser(user)
    }
}
