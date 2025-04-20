package com.example.antique.view.screens.login

import android.annotation.SuppressLint
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

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ResetPassword(
    navController: NavHostController,
    viewModel: ForgotViewModel
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        // Modifier for click action anywhere on the screen - Hide keyboard and reset focus
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )
        {
            keyboardController?.hide()
            focusManager.clearFocus()

        },

        topBar = { TopBar(navBack = { navController.popBackStack() }) }
    ) { padding ->
        Column(

            modifier = Modifier
                .padding(padding)
                .padding(20.dp),
            //horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "Reset Password",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            Text(
                text = "Please Enter your new Password"
            )
            Spacer(modifier = Modifier.height(102.dp))
            Text(
                text = "Enter new Password"
            )
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Re-Enter new Password"
            )
            OutlinedTextField(
                value = viewModel.rePassword,
                onValueChange = { viewModel.rePassword = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )
            fun resetPassword() {
                if(viewModel.passwordMatch()){
                    if(viewModel.resetPassword())
                        navController.navigate(Screen.Login.route)
                    else
                        Toast
                            .makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                }else{
                    Toast
                        .makeText(context, "Passwords don't match!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    onClick = {
                        resetPassword()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Reset Password",
                    )
                }
            }

        }
    }

}
