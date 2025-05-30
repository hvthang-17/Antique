package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.remote.entity.User
import com.example.antique.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import java.util.Properties
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random
import com.example.antique.BuildConfig



class ForgotViewModel(val context: Application) : AndroidViewModel(context) {
    private val userRepository = UserRepository
    private var currentUser: User? = null
    private var verificationCode: Int? = null

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var rePassword by mutableStateOf("")
    var vCode by mutableStateOf("")
    private var error = false

    fun validateEmailInput(onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserByEmail(email)
            error = user == null
            currentUser = user
            onResult(user != null)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
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

    fun resetPassword(onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (currentUser == null) {
                    println("⚠️ currentUser is null")
                    onResult(false)
                    return@launch
                }

                currentUser?.let {
                    println("🔐 Resetting password for user: ${it.email}")
                    it.password = password
                    userRepository.updateUser(it)

                    val reFetchedUser = userRepository.getUserByEmail(email)
                    val success = reFetchedUser?.password == password

                    println("✅ Reset password success: $success")
                    onResult(success)
                }
            } catch (e: Exception) {
                println("❌ Exception in resetPassword: ${e.message}")
                onResult(false)
            }
        }
    }


    fun sendVerificationEmail(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val props = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                }

                val username = BuildConfig.EMAIL_USERNAME
                val password = BuildConfig.EMAIL_PASSWORD

                println("USERNAME: $username")  // ✅ Debug
                println("PASSWORD: $password")  // ✅ Debug


                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(username, password)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(username))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
                    subject = "Mã xác thực khôi phục mật khẩu"
                    setHeader("Content-Type", "text/plain; charset=UTF-8")
                    setText("Mã xác thực của bạn là: $verificationCode", "UTF-8")
                }

                Transport.send(message)
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

}