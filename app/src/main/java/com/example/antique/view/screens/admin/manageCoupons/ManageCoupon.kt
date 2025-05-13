package com.example.antique.view.screens.admin.manageCoupons

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.CouponViewModel
import com.example.antique.viewmodel.HomeViewModel
import com.example.antique.viewmodel.admin.ManageCouponVM
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManageCouponInit(
    navController: NavHostController,
    actionType: String?
) {
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)
    ManageCoupon(
        navController,
        actionType = actionType!!
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageCoupon(
    navController: NavHostController,
    actionType: String = "",
    @SuppressLint("ContextCastToActivity") viewModel: ManageCouponVM = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") couponVM: CouponViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var btnText = "Lưu"
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val calendar = remember { Calendar.getInstance() }

    val dateDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                viewModel.expiryDate = sdf.format(calendar.time)
                viewModel.expiryDateError = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
        }
    }

    fun validateInputs(): Boolean {
        if (actionType == "Thêm") {
            if (viewModel.code.isBlank()) {
                Toast.makeText(context, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        if (viewModel.discountPercent <= 0) {
            Toast.makeText(context, "Phần trăm giảm giá phải lớn hơn 0", Toast.LENGTH_SHORT).show()
            return true
        }

        if (viewModel.expiryDate.isBlank()) {
            Toast.makeText(context, "Vui lòng chọn ngày hết hạn", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }


    Scaffold(
        modifier = Modifier
            .background(Color(0xFFF8EBCB))
            .clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        topBar = { TopBar("$actionType mã giảm giá", { navController.popBackStack(); }) },
        content = { padding ->
            Column(
                Modifier
                    .background(Color(0xFFF8EBCB))
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.code,
                        onValueChange = {
                            viewModel.code = it
                        },
                        label = { Text("Mã giảm giá") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6D4C41),
                        unfocusedBorderColor = Color(0xFF6D4C41),
                        cursorColor = Color(0xFF8B5E3C)
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.discountPercent.toString(),
                        onValueChange = {
                            viewModel.discountPercent = it.toIntOrNull() ?: 0
                        },
                        label = { Text("Phần trăm giảm giá") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6D4C41),
                            unfocusedBorderColor = Color(0xFF6D4C41),
                            cursorColor = Color(0xFF8B5E3C)
                        )
                    )

                    OutlinedTextField(
                        value = viewModel.expiryDate,
                        onValueChange = {},
                        label = { Text("Ngày hết hạn") },
                        singleLine = true,
                        enabled = false,
                        isError = viewModel.expiryDateError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                dateDialog.show()
                            },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color(0xFF6D4C41),
                            focusedBorderColor = Color(0xFF6D4C41),
                            unfocusedBorderColor = Color(0xFF6D4C41),
                            disabledTextColor =Color(0xFF8B5E3C),
                            cursorColor = Color(0xFF8B5E3C)

                        )
                    )

                    Button(
                        onClick = {
                            if (!validateInputs()) {
                                if (actionType == "Chỉnh sửa") viewModel.updateCoupon()
                                else viewModel.addCoupon()

                                couponVM.getCoupons()
                                navController.navigate(Screen.AdminCouponList.route)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B5E3C),
                            contentColor = Color.White
                        )
                    ) {
                        if (actionType == "Thêm") {
                            btnText = "Thêm"
                        }
                        Text(text = btnText)
                    }
                }
            }
        },
        bottomBar = { AdminBottomBar(navController = navController) })
}
