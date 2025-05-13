package com.example.antique.view.screens.admin.manageCategories

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import coil.compose.rememberAsyncImagePainter
import com.example.antique.model.remote.utils.CloudinaryManager
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.CategoryViewModel
import com.example.antique.viewmodel.HomeViewModel
import com.example.antique.viewmodel.admin.ManageCategoryVM

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryInit(
    navController: NavHostController,
    actionType: String?
) {
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)
    ManageCategory(
        navController,
        actionType = actionType!!
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageCategory(
    navController: NavHostController,
    actionType: String = "",
    @SuppressLint("ContextCastToActivity") viewModel: ManageCategoryVM = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") categoryVM: CategoryViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var btnText = "Lưu"
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val woodBrown = Color(0xFF8B5E3C)
    val parchment = Color(0xFFF8EBCB)
    val borderBrown = Color(0xFF6D4C41)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            CloudinaryManager.uploadImage(
                context = context,
                fileUri = it,
                onSuccess = { uploadedUrl ->
                    viewModel.imageUrl = uploadedUrl
                    Toast.makeText(context, "Upload thành công!", Toast.LENGTH_SHORT).show()
                },
                onError = { errorMessage ->
                    Toast.makeText(context, "Upload thất bại: $errorMessage", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
    }

    fun validateInputs(): Boolean {
        if (actionType == "Thêm") {
            if (viewModel.cid == 0) {
                Toast.makeText(context, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        if (viewModel.name.isBlank()) {
            Toast.makeText(context, "Tên danh mục không thể trống", Toast.LENGTH_SHORT).show()
            return true
        }

        if (viewModel.description.isBlank()) {
            Toast.makeText(context, "Mô tả không thể trống", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }


    Scaffold(
        modifier = Modifier
            .clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        topBar = { TopBar("$actionType danh mục", { navController.popBackStack(); }) },
        content = { padding ->
            Column(
                Modifier
                    .background(parchment)
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    if (viewModel.imageUrl.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Ảnh danh mục",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(viewModel.imageUrl),
                            contentDescription = "Ảnh danh mục",
                            modifier = Modifier
                                .size(200.dp)
                                .clickable { imagePickerLauncher.launch("image/*") }
                        )
                    }

                    OutlinedTextField(
                        value = viewModel.imageUrl,
                        onValueChange = { viewModel.imageUrl = it },
                        label = { Text("URL ảnh") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = borderBrown,
                            unfocusedBorderColor = borderBrown,
                            cursorColor = woodBrown
                        )
                    )

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = woodBrown, contentColor = Color.White)
                    ) {
                        Text("Chọn ảnh từ thiết bị")
                    }

                    OutlinedTextField(
                        value = viewModel.cid.toString(),
                        onValueChange = { viewModel.cid = it.toIntOrNull() ?: 0 },
                        label = { Text("Mã danh mục") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = borderBrown,
                            unfocusedBorderColor = borderBrown,
                            cursorColor = woodBrown
                        )
                    )

                    OutlinedTextField(
                        value = viewModel.name,
                        onValueChange = { viewModel.name = it },
                        label = { Text("Tên danh mục") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = borderBrown,
                            unfocusedBorderColor = borderBrown,
                            cursorColor = woodBrown
                        )
                    )

                    OutlinedTextField(
                        value = viewModel.description,
                        onValueChange = { viewModel.description = it },
                        label = { Text("Mô tả") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(80.dp, 300.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = borderBrown,
                            unfocusedBorderColor = borderBrown,
                            cursorColor = woodBrown
                        )
                    )

                    Button(
                        onClick = {
                            if (!validateInputs()) {
                                if (actionType == "Chỉnh sửa") viewModel.updateCategory()
                                else viewModel.addCategory()

                                categoryVM.getCategories()
                                navController.navigate(Screen.AdminCategoryList.route)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = woodBrown, contentColor = Color.White)
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