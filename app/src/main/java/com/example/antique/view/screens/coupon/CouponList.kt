package com.example.antique.view.screens.coupon

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.model.remote.entity.Coupon
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.CouponViewModel
import com.example.antique.viewmodel.HomeViewModel
import com.example.antique.viewmodel.admin.ManageCouponVM

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponList(
    navController: NavHostController,
    title: String?,
    type: String?,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    homeViewModel: HomeViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val couponVM = viewModel<CouponViewModel>(LocalContext.current as ComponentActivity)
    val manageCouponVM = viewModel<ManageCouponVM>(LocalContext.current as ComponentActivity)

    val couponList = couponVM.coupons.value
    val navRoute = remember { mutableStateOf("/-1/$title") }

    LaunchedEffect(Unit) {
        couponVM.getCoupons()
    }

    Scaffold(
        topBar = {
            TopBar(title ?: "", { navController.popBackStack() }, actions = {
                IconButton(onClick = {
                    if (type == "Admin") {
                        manageCouponVM.resetCurrentCoupon()
                        navController.navigate("manageCoupon/Thêm")
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
                if (couponList.isEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Không tìm thấy mã!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Button(onClick = { navController.navigate("home") }) {
                            Text("Quay lại trang chủ")
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(all = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(items = couponList, key = { it.id }) { coupon ->
                            CouponListCard(
                                coupon = coupon,
                                navController = navController,
                                isAdmin = appViewModel.isAdmin,
                                onEdit = {
                                    manageCouponVM.setCurrentCoupon(coupon)
                                    navController.navigate("manageCoupon/Chỉnh sửa")
                                },
                                onDelete = {
                                    couponVM.deleteCoupon(coupon.id)
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
fun CouponListCard(
    coupon: Coupon,
    navController: NavHostController,
    isAdmin: Boolean = false,
    onEdit: (Coupon) -> Unit,
    onDelete: (Coupon) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận xóa", color = Color(0xFF8B5E3C)) },
            text = { Text("Bạn có chắc muốn xóa mã này không?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(coupon)
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
            containerColor = Color.White
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9C789)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Mã khuyến mãi: ${coupon.code}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF4B1E1E)
                )
                Text(
                    text = "Phần trăm giảm giá: ${coupon.discountPercent}%",
                    fontSize = 16.sp,
                    color = Color(0xFF6D4C41)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ngày hết hạn: ${coupon.expiryDate}",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isAdmin) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { onEdit(coupon) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Sửa",
                                tint = Color(0xFF8B5E3C)
                            )
                        }
                        IconButton(onClick = { showDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
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


