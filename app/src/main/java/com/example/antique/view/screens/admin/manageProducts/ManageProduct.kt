package com.example.antique.view.screens.admin.manageProducts

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import com.example.antique.view.components.DropDownMenu
import com.example.antique.view.components.TopBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.HomeViewModel
import com.example.antique.viewmodel.ProductViewModel
import com.example.antique.viewmodel.admin.ManageProductVM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.antique.model.remote.entity.Category
import java.util.Locale


@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManageProductInit(
    navController: NavHostController,
    actionType: String?
) {
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)
    val categoryList = remember { homeVM.getAllCategories() }
    ManageProduct(
        navController,
        categoryList = categoryList,
        actionType = actionType!!
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageProduct(
    navController: NavHostController,
    categoryList: List<Any> = emptyList(),
    actionType: String = "",
    @SuppressLint("ContextCastToActivity") viewModel: ManageProductVM = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") productVM: ProductViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var btnText = "Lưu"
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
                    Toast.makeText(context, "Upload thất bại: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }




    fun validateInputs(): Boolean {
        if (actionType == "Add") if (viewModel.cid.isBlank() || viewModel.cid == "-1") {
            Toast.makeText(context, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show()
            return true
        }


        if (viewModel.stock.isBlank() || viewModel.price.isBlank() || viewModel.description.isBlank() || viewModel.title.isBlank()) {
            Toast.makeText(context, "Các dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }

    Scaffold(modifier = Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() }, indication = null
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
    },
        topBar = { TopBar("$actionType Sản phẩm", { navController.popBackStack(); }) },
        content = { padding ->
            Column(
                Modifier
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
                            text = "Ảnh sản phẩm",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(viewModel.imageUrl),
                            contentDescription = "Ảnh sản phẩm",
                            modifier = Modifier
                                .size(200.dp)
                                .clickable { imagePickerLauncher.launch("image/*") }
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(50.dp, 200.dp),
                        value = viewModel.imageUrl,
                        onValueChange = { viewModel.imageUrl = it },
                        label = { Text("URL") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Chọn ảnh từ thiết bị")
                    }

                    CategoryDropDown(categoryList as List<Category>, viewModel.cid)

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.title,
                        onValueChange = {
                            viewModel.title = it
                        },
                        label = { Text("Tên sản phẩm") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    OutlinedTextField(
                        value = viewModel.price,
                        onValueChange = {
                            viewModel.price = it
                        },
                        label = { Text("Giá sản phẩm") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(50.dp, 300.dp),
                        value = viewModel.description,
                        onValueChange = { viewModel.description = it },
                        label = { Text("Mô tả") },
                        placeholder = { },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.stock,
                        onValueChange = { viewModel.stock = it },
                        label = { Text("Số lượng") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Button(
                        onClick = {

                            if (!validateInputs()) {
                                if (actionType == "Edit") viewModel.updateProduct()
                                else viewModel.addProduct()

                                productVM.getProducts()
                                navController.navigate(Screen.AdminProductList.route)
                            }
                        }, Modifier.align(End)
                    ) {
                        if (actionType == "Add") {
                            btnText = "Thêm"
                        }
                        Text(text = btnText)
                    }
                }
            }
        },
        bottomBar = { AdminBottomBar(navController = navController) })
}

@Composable
fun CategoryDropDown(list: List<Category>, cid: String) {
    var expanded by remember { mutableStateOf(false) }
    val selectedIndex = list.indexOfFirst { it.cid == cid.toIntOrNull() }
    val initialCategoryName = if (selectedIndex != -1) list[selectedIndex].name else "Not Selected"
    var selectedText by remember { mutableStateOf(initialCategoryName) }

    val context = LocalContext.current
    val viewModel: ManageProductVM = viewModel(context as ComponentActivity)

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        selectedText = category.name
                        viewModel.cid = category.cid.toString()
                        expanded = false
                    }
                )
            }
        }
    }
}

