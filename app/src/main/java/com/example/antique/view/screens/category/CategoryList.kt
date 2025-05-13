package com.example.antique.view.screens.category

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.antique.model.remote.entity.Category
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.CategoryViewModel
import com.example.antique.viewmodel.HomeViewModel
import com.example.antique.viewmodel.admin.ManageCategoryVM

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryList(
    navController: NavHostController,
    title: String?,
    type: String?,
    cid: String?,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    homeViewModel: HomeViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val categoryVM = viewModel<CategoryViewModel>(LocalContext.current as ComponentActivity)
    val manageCategoryVM = viewModel<ManageCategoryVM>(LocalContext.current as ComponentActivity)
    val woodBrown = Color(0xFF8B5E3C)
    val categoryList = categoryVM.categories.value
    val errorMessage = categoryVM.errorMessage.value
    val navRoute = remember { mutableStateOf("/-1/$title") }

    LaunchedEffect(Unit) {
        categoryVM.getCategories()
    }

    Scaffold(
        topBar = {
            TopBar(title ?: "", { navController.popBackStack() }, actions = {
                IconButton(onClick = {
                    if (type == "Admin") {
                        // Khi click Thêm, reset danh mục hiện tại và điều hướng đến màn thêm
                        manageCategoryVM.resetCurrentCategory()
                        navController.navigate("manageCategory/Thêm")
                    } else {
                        navController.navigate(navRoute.value)
                    }
                }) {
                    val icon = if (type == "Admin") Icons.Filled.Add else Icons.Filled.FilterList
                    Icon(imageVector = icon, contentDescription = "Thêm")
                }
            })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF8EBCB)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                if (categoryList.isEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Không tìm thấy danh mục!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Button(
                            onClick = { navController.navigate("home") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = woodBrown,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Quay lại trang chủ")
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(all = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(items = categoryList, key = { it.id }) { category ->
                            CategoryListCard(
                                category = category,
                                navController = navController,
                                isAdmin = appViewModel.isAdmin,
                                onEdit = {
                                    manageCategoryVM.setCurrentCategory(category)
                                    navController.navigate("manageCategory/Chỉnh sửa")
                                },
                                onDelete = {
                                    categoryVM.deleteCategory(category.id)
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        }
    )

}

@Composable
fun CategoryListCard(
    category: Category,
    navController: NavHostController,
    isAdmin: Boolean = false,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val parchment = Color(0xFFF8EBCB)
    val borderBrown = Color(0xFF6D4C41)

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc muốn xóa danh mục '${category.name}' không?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(category)
                    showDialog = false
                }) {
                    Text("Xóa", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            },
            containerColor = parchment
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9C789)),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = category.image),
                contentDescription = "Ảnh danh mục",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF4B1E1E)
                )
                Text(
                    text = "Mã: ${category.cid}",
                    fontSize = 16.sp,
                    color = borderBrown
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.description ?: "Không có mô tả",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isAdmin) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { onEdit(category) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Sửa",
                                tint = Color(0xFF6D4C41)
                            )
                        }
                        IconButton(
                            onClick = { showDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Xóa",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}
