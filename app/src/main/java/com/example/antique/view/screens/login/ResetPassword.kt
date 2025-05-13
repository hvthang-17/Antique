package com.example.antique.view.screens.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.antique.R
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
                .fillMaxSize()
                .background(Color(0xFFD9C789))
                .padding(20.dp)
        ) {
            Text(
                text = "Đặt lại mật khẩu",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color(0xFF4B1E1E)
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Vui lòng nhập mật khẩu mới của bạn",
                color = Color(0xFF4B1E1E)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.reset),
                    contentDescription = "",
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "Mật khẩu mới",
                color = Color(0xFF4B1E1E)
            )

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text(text = "Mật khẩu") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Nhập lại mật khẩu",
                color = Color(0xFF4B1E1E)
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
                            .makeText(context,  "Có lỗi xảy ra", Toast.LENGTH_SHORT)
                            .show()
                }else{
                    Toast
                        .makeText(context, "Mật khẩu không khớp!", Toast.LENGTH_SHORT)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8C6A3F),
                        contentColor = Color.White
                    )
                ) {
                    Text("Đặt lại mật khẩu", fontSize = 16.sp)
                }
            }

        }
    }

}
