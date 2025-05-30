package com.example.antique.view.screens.login

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
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        topBar = { TopBar(navBack = { navController.popBackStack() }) }
    ) { padding ->

        val brownText = Color(0xFF4B1E1E)
        val buttonColor = Color(0xFF8C6A3F)

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFD9C789))
                .padding(20.dp),
        ) {
            Text(
                text = "Xác minh mã",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = brownText
            )

            Text(
                text = "Vui lòng nhập mã xác thực được gửi đến email của bạn.",
                color = brownText
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.verify),
                    contentDescription = null,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // ❗ Chỉ hiển thị mã khi đang DEBUG
            /*
            if (BuildConfig.DEBUG) {
                Text(
                    text = "Mã xác thực hệ thống: ${viewModel.getVCode()}",
                    color = brownText,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            */

            Text(
                text = "Nhập mã xác thực",
                color = brownText,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = viewModel.vCode,
                onValueChange = { viewModel.vCode = it },
                label = { Text(text = "Mã xác thực") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            fun submitVCode() {
                keyboardController?.hide()
                focusManager.clearFocus()

                if (viewModel.vCode.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập mã xác thực!", Toast.LENGTH_SHORT).show()
                    return
                }

                if (viewModel.verify()) {
                    Toast.makeText(context, "Xác thực thành công!", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.ResetPassword.route)
                } else {
                    Toast.makeText(context, "Mã xác thực không đúng!", Toast.LENGTH_SHORT).show()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { submitVCode() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    )
                ) {
                    Text("Xác nhận")
                }
            }
        }
    }
}
