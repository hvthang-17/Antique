package com.example.antique.view.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antique.view.components.DatePickerDialog
import com.example.antique.view.components.SuccessDialog
import com.example.antique.view.components.TopBar
import com.example.antique.viewmodel.RegisterViewModel
import java.util.*

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Register(
    navToLogin: () -> Unit,
    @SuppressLint("ContextCastToActivity") viewModel: RegisterViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val inputFieldModifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val antiqueBackground = Color(0xFFD9C789)
    val brownText = Color(0xFF4B1E1E)
    val borderColor = Color(0xFF8C6A3F)
    val buttonColor = Color(0xFF8C6A3F)

    Scaffold(
        // Modifier for click action anywhere on the screen - Hide keyboard and reset focus
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },

        topBar = { TopBar(navBack = { navToLogin() }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(antiqueBackground)
                .padding(20.dp)
        ) {
            val context = LocalContext.current

            Text(
                "Tạo tài khoản",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = brownText
            )

            Text(
                "Tham gia cùng chúng tôi và bắt đầu mua sắm ngay!",
                fontSize = 15.sp,
                color = brownText
            )

            OutlinedTextField(
                modifier = inputFieldModifier,
                value = viewModel.firstName,
                onValueChange = {
                    if (viewModel.fNameError) viewModel.fNameError = false
                    viewModel.firstName = it
                },
                label = { Text("Họ", color = brownText) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = viewModel.fNameError,
            )

            OutlinedTextField(
                modifier = inputFieldModifier,
                value = viewModel.lastName,
                onValueChange = {
                    if (viewModel.lNameError) viewModel.lNameError = false
                    viewModel.lastName = it
                },
                label = { Text("Tên", color = brownText) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = viewModel.lNameError,
            )

            OutlinedTextField(
                modifier = inputFieldModifier,
                value = viewModel.email,
                onValueChange = {
                    if (viewModel.emailError) viewModel.emailError = false
                    viewModel.email = it
                },
                label = { Text("Email", color = brownText) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = viewModel.emailError,
            )

            OutlinedTextField(
                modifier = inputFieldModifier,
                value = viewModel.password,
                onValueChange = {
                    if (viewModel.passwordError) viewModel.passwordError = false
                    viewModel.password = it
                },
                label = { Text("Mật khẩu", color = brownText) },
                singleLine = true,
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                isError = viewModel.passwordError,
                trailingIcon = {
                    val image =
                        if (viewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (viewModel.passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    IconButton(onClick = {
                        viewModel.passwordVisible = !viewModel.passwordVisible
                    }) {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = brownText
                        )
                    }
                },
            )

            // Date Picker
            val calendar = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }
            val dateDialog = DatePickerDialog(
                viewModel.birthDate, context, calendar, calendar.timeInMillis
            )

            OutlinedTextField(
                value = viewModel.birthDate.value,
                modifier = inputFieldModifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                    dateDialog.show()
                },
                onValueChange = {},
                label = { Text("Ngày sinh", color = brownText) },
                singleLine = true,
                enabled = false,
                isError = viewModel.dobError,
            )

            // Checkbox + Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TermsCheck(viewModel.termsIsChecked, brownText)

                val openDialog = remember { mutableStateOf(false) }

                Button(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = viewModel.termsIsChecked.value,
                    onClick = {
                        if (!viewModel.validateRegisterInput()) {
                            Toast.makeText(context, "Thông tin không hợp lệ!", Toast.LENGTH_SHORT)
                                .show()
                        } else if (!viewModel.validateExistingAccount()) {
                            Toast.makeText(context, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.addUser()
                            openDialog.value = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("ĐĂNG KÝ", color = Color.White, fontSize = 18.sp)
                }

                SuccessDialog(
                    openDialog,
                    "Tạo tài khoản thành công",
                    "Chúc bạn mua sắm vui vẻ!",
                    "Đăng nhập"
                ) {
                    navToLogin()
                }
            }
        }
    }
}

@Composable
fun TermsCheck(termsIsChecked: MutableState<Boolean>, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = termsIsChecked.value,
            onCheckedChange = { termsIsChecked.value = it },
            colors = CheckboxDefaults.colors(checkedColor = color, uncheckedColor = color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            "Tôi đồng ý với Điều khoản dịch vụ & Chính sách bảo mật.",
            fontSize = 12.sp,
            color = color,
            modifier = Modifier.clickable {
                termsIsChecked.value = !termsIsChecked.value
            }
        )
    }
}