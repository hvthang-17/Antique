package com.example.antique.view.screens.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antique.R
import com.example.antique.view.components.toAdaptiveDp
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.LoginViewModel
import androidx.compose.foundation.background


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Login(
    onSuccessfulLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotClick: () -> Unit,
    @SuppressLint("ContextCastToActivity") appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") viewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9C789))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            fun handleLogin() {
                if (viewModel.validateLoginInput()) {
                    if (viewModel.authenticateLogin()) {
                        appViewModel.setCurrentUserId(viewModel.getCurrentUserId())
                        viewModel.password = ""
                        onSuccessfulLogin()
                    } else {
                        Toast.makeText(
                            context,
                            "Thông tin đăng nhập không hợp lệ!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Thiếu thông tin đăng nhập!", Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.signin),
                    contentDescription = "",
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "ĐĂNG NHẬP",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B1E1E),
            )

            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = {
                    if (viewModel.emailError) viewModel.emailError = false
                    viewModel.email = it
                },
                modifier = Modifier
                    .padding(top = 20.toAdaptiveDp())
                    .fillMaxWidth(),
                label = { Text(text = "Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = viewModel.emailError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B1E1E),
                    unfocusedBorderColor = Color(0xFF4B1E1E),
                    focusedContainerColor = Color(0xFFD9C789),
                    unfocusedContainerColor = Color(0xFFD9C789)
                )
            )

            val inputFieldModifier = Modifier
                .padding(top = 10.toAdaptiveDp())
                .fillMaxWidth()

            OutlinedTextField(
                value = viewModel.password,
                modifier = inputFieldModifier,
                onValueChange = {
                    if (viewModel.passwordError) viewModel.passwordError = false
                    viewModel.password = it
                },
                label = { Text("Mật khẩu") },
                singleLine = true,
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    handleLogin()
                }),
                isError = viewModel.passwordError,
                trailingIcon = {
                    val image =
                        if (viewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (viewModel.passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = {
                        viewModel.passwordVisible = !viewModel.passwordVisible
                    }) {
                        Icon(imageVector = image, description)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B1E1E),
                    unfocusedBorderColor = Color(0xFF4B1E1E),
                    focusedContainerColor = Color(0xFFD9C789),
                    unfocusedContainerColor = Color(0xFFD9C789)
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Quên mật khẩu?",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF014074)),
                    modifier = Modifier.clickable { onForgotClick() }
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { handleLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8C6A3F),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đăng nhập", fontSize = 16.sp)
            }


            Spacer(Modifier.height(16.dp))

            Text(
                text = "Bạn chưa có tài khoản?",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Đăng ký ngay",
                style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF014074)),
                modifier = Modifier.clickable { onRegisterClick() }
            )

            Spacer(Modifier.height(24.dp))

            Text("hoặc đăng nhập với")

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(R.drawable.google, R.drawable.facebook, R.drawable.twitter).forEach {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
        }
    }
}
