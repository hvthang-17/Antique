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
import android.os.Handler
import android.os.Looper


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Forgot(
    navController: NavHostController,
    viewModel: ForgotViewModel,
    onVerifyClick: () -> Unit
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

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFD9C789))
                .padding(20.dp)
        ) {

            Text(
                text = "Quên mật khẩu",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = brownText
            )

            Text(
                text = "Vui lòng nhập địa chỉ email. Mã xác thực sẽ được gửi đến bạn.",
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
                    painter = painterResource(id = R.drawable.forgot),
                    contentDescription = "",
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "Địa chỉ email",
                fontWeight = FontWeight.SemiBold,
                color = brownText
            )

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email", color = brownText) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        viewModel.validateEmailInput { isValid ->
                            if (isValid) {
                                viewModel.generateVCode()

                                viewModel.sendVerificationEmail(
                                    onSuccess = {
                                        // Gọi lại trên Main Thread
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(context, "Mã xác thực đã được gửi đến email!", Toast.LENGTH_SHORT).show()
                                            navController.navigate(Screen.VerifyPassword.route)
                                        }
                                    },
                                    onFailure = { e ->
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(context, "Lỗi khi gửi email: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                )

                            } else {
                                Toast.makeText(context, "Email không tồn tại trong hệ thống!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8C6A3F),
                        contentColor = Color.White
                    )
                ) {
                    Text("Gửi mã xác thực", fontSize = 16.sp)
                }
            }
        }
    }
}
