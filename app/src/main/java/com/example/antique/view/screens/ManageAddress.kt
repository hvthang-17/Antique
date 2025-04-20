package com.example.antique.view.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.view.components.TopBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.ManageAddressViewModel

@SuppressLint("ContextCastToActivity")
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageAddress(
    navController: NavHostController,
    initScreen: String?,
    addressId: String,
    viewModel: ManageAddressViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    viewModel.setUser(
        viewModel<AppViewModel>(LocalContext.current as ComponentActivity).getCurrentUserId(),
    )

    val inputFieldModifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var manageType = ""
    if (addressId == "-1") {
        manageType = "Thêm"
        viewModel.resetFields()
    } else {
        manageType = "Sửa"
        viewModel.setCurrentAddress(addressId)
    }

    Scaffold(
        // Modifier for click action anywhere on the screen - Hide keyboard and reset focus
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },

        topBar = { TopBar("$manageType địa chỉ", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {

                val context = LocalContext.current
                OutlinedTextField(
                    value = viewModel.contactName,
                    onValueChange = {
                        if (viewModel.nameError) viewModel.nameError = false
                        viewModel.contactName = it
                    },
                    label = { Text("Họ và tên người nhận") },
                    isError = viewModel.nameError,
                    singleLine = true,
                    modifier = inputFieldModifier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = viewModel.contactNumber,
                    onValueChange = {
                        viewModel.contactNumber = it
                        viewModel.numberError = false
                    },
                    label = { Text("Số điện thoại") },
                    isError = viewModel.numberError,
                    singleLine = true,
                    modifier = inputFieldModifier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                OutlinedTextField(
                    value = viewModel.street,
                    onValueChange = {
                        viewModel.street = it
                        viewModel.streetError = false
                    },
                    label = { Text("Tên đường)") },
                    isError = viewModel.streetError,
                    modifier = inputFieldModifier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = viewModel.ward,
                    onValueChange = {
                        viewModel.ward = it
                        viewModel.wardError = false
                    },
                    label = { Text("Phường / Xã") },
                    isError = viewModel.wardError,
                    modifier = inputFieldModifier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = viewModel.district,
                    onValueChange = {
                        viewModel.district = it
                        viewModel.districtError = false
                    },
                    label = { Text("Quận / Huyện") },
                    isError = viewModel.districtError,
                    modifier = inputFieldModifier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = viewModel.city,
                    onValueChange = {
                        viewModel.city = it
                        viewModel.cityError = false
                    },
                    label = { Text("Tỉnh / Thành phố") },
                    isError = viewModel.cityError,
                    modifier = inputFieldModifier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = viewModel.poBox,
                    onValueChange = {
                        viewModel.poBox = it
                        viewModel.poBoxError = false
                    },
                    label = { Text("Mã bưu điện (nếu có)") },
                    isError = viewModel.poBoxError,
                    modifier = inputFieldModifier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    Button(
                        onClick = {
                            if (!viewModel.validateAddress()) {
                                Toast.makeText(context, "Invalid input!", Toast.LENGTH_SHORT).show()
                            } else {
                                if (manageType == "Thêm") viewModel.addAddress()
                                else if (manageType == "Sửa") viewModel.updateAddress(addressId)

                                if (initScreen == "Select") {
                                    navController.navigate(Screen.SelectAddress.route) {
                                        popUpTo(Screen.SelectAddress.route) { inclusive = true }
                                    }
                                } else if (initScreen == "Manage") {
                                    navController.navigate(Screen.MyAddresses.route) {
                                        popUpTo(Screen.MyAddresses.route) { inclusive = true }
                                    }
                                }
                            }
                        }, shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = manageType)
                    }
                }
            }
        })
}

