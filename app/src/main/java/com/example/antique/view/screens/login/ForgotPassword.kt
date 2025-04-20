package com.example.antique.view.screens.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.antique.view.components.TopBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.ForgotViewModel

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forgot(
    navController: NavHostController, viewModel: ForgotViewModel, onVerifyClick: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        // Modifier for click action anywhere on the screen - Hide keyboard and reset focus
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }, topBar = { TopBar(navBack = { navController.popBackStack() }) }, content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp),
            ) {
                Text(
                    text = "Forgot Password", fontWeight = FontWeight.Bold, fontSize = 40.sp
                )
                Text(
                    text = "Please Enter your email address, a verification code will be sent to your email"
                )
                Spacer(modifier = Modifier.height(102.dp))
                Text(
                    text = "Email address"
                )
                OutlinedTextField(value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                fun sendVerificationCode() {
                    if (viewModel.validateEmailInput()) {
                        viewModel.generateVCode()
                        navController.navigate(Screen.VerifyPassword.route)
                    } else {
                        Toast.makeText(context, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            sendVerificationCode()
                        }, modifier = Modifier.fillMaxWidth()

                    ) {
                        Text(
                            text = "Send verification code"
                        )
                    }
                }
            }
        })
}






