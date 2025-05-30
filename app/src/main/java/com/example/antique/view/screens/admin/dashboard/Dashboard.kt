package com.example.antique.view.screens.admin.dashboard

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.model.remote.entity.Category
import com.example.antique.model.remote.entity.Coupon
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.Product
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.navigation.Screen
import com.example.antique.view.theme.largeTitle
import com.example.antique.viewmodel.OrderViewModel
import com.example.antique.viewmodel.admin.DashboardVM
import kotlin.math.round

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardInit(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val adminDashboardVM = viewModel<DashboardVM>(context as ComponentActivity)
    val orderVM = viewModel<OrderViewModel>(context)

    val allOrders = orderVM.getAllOrders()
    val totalOrders = allOrders.size
    val totalSales = adminDashboardVM.getTotalSales()
    val allProducts = remember { adminDashboardVM.getAllProducts() }
    val allCategories = remember { adminDashboardVM.getAllCategories() }
    val allCoupons = remember { adminDashboardVM.getAllCoupons() }

    adminDashboardVM.coupons
    adminDashboardVM.categories
    adminDashboardVM.products
    adminDashboardVM.allOrders = allOrders.toMutableList()
    val latestOrders = adminDashboardVM.getLatestOrders()
    val latestOrdersSubList = if (latestOrders.size < 10)
        latestOrders else latestOrders.subList(latestOrders.size - 10, latestOrders.size)

    Dashboard(
        navController,
        latestOrdersSubList,
        totalOrders,
        totalSales,
        allProducts,
        allCategories,
        allCoupons
    )
}

@ExperimentalMaterial3Api
@Composable
fun Dashboard(
    navController: NavHostController,
    orderData: List<Order>,
    totalOrders: Int,
    totalSales: Double,
    products: List<Product>,
    categories: List<Category>,
    coupons: List<Coupon>,
) {
    Scaffold(
        containerColor = Color(0xFFF8EBCB),
        topBar = { TopBar("Trang tổng quan") },
        bottomBar = { AdminBottomBar(navController = navController) },
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatsCard(Icons.Filled.ListAlt, "Tổng đơn hàng", "$totalOrders") {
                        navController.navigate("detailsPage/orders/Tổng đơn hàng")
                    }
                    StatsCard(Icons.Filled.Payments, "Tổng doanh thu", "$${round(totalSales)}") {
                        navController.navigate("detailsPage/sales/Tổng doanh thu")
                    }
                    StatsCard(Icons.Filled.Store, "Tổng sản phẩm", "${products.size}") {
                        navController.navigate(Screen.AdminProductList.route)
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatsCard(Icons.Default.Category, "Tổng danh mục", "${categories.size}") {
                        navController.navigate(Screen.AdminCategoryList.route)
                    }
                    StatsCard(Icons.Filled.Discount, "Mã giảm giá", "${coupons.size}") {
                        navController.navigate(Screen.AdminCouponList.route)
                    }
                    StatsCard(Icons.Filled.BarChart, "Doanh thu", "") {
                        navController.navigate(Screen.AdminSalesChart.route)
                    }
                }

                Divider(color = Color(0xFF6D4C41), thickness = 1.dp)

                Text(
                    text = "Các đơn hàng gần đây",
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .offset(x = 12.dp),
                    textAlign = TextAlign.Left,
                    style = largeTitle,
                    color = Color(0xFF4B1E1E)
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SegmentedButton()
                }

                Divider(color = Color(0xFF6D4C41), thickness = 1.dp)

                LazyColumn(
                    contentPadding = PaddingValues(all = 10.dp)
                ) {
                    items(items = orderData) { order ->
                        LatestOrderCard(navController, order = order)
                    }
                }
            }
        }
    )
}


