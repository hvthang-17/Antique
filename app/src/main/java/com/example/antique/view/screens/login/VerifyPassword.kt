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
fun VerifyPassword(
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
                text = "Verification",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            Text(
                text = "Please Enter the verification code sent to your email"
            )
            Spacer(modifier = Modifier.height(102.dp))
            Text(text = "Generated Verification Code: ")
            Text(text = viewModel.getVCode().toString())
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Verification code"
            )
            OutlinedTextField(
                value = viewModel.vCode,
                onValueChange = { viewModel.vCode = it },
                label = { Text(text = "Verification code") },
                modifier = Modifier.fillMaxWidth()
            )

            fun submitVCode(){
                if(viewModel.verify()){
                    navController.navigate(Screen.ResetPassword.route)
                }else{
                    Toast
                        .makeText(context, "Invalid verification code", Toast.LENGTH_SHORT)
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
                        submitVCode()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Submit",
                    )
                }
            }

        }
    }

}