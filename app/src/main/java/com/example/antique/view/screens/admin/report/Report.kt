package com.example.antique.view.screens.admin.report

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.Product
import com.example.antique.model.remote.entity.TotalProductInfo
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.navigation.Screen
import com.example.antique.view.theme.*
import com.example.antique.viewmodel.OrderViewModel
import com.example.antique.viewmodel.admin.DashboardVM
import com.example.antique.viewmodel.admin.ReportVM
import kotlin.math.round

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ContextCastToActivity")
@Composable
fun AdminReportsInit(navController: NavHostController) {

    val adminDashboardVM = viewModel<DashboardVM>(LocalContext.current as ComponentActivity)
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)
    val products = remember { adminDashboardVM.getAllProducts() }
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)
    var orders = emptyList<Order>()
    reportVM.totalProductsByStatus = emptyList<TotalProductInfo>().toMutableList()

    if (reportVM.isFiltered && reportVM.filteredOrders.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Không có đơn hàng trong phạm vi này!")
            TextButton(onClick = {
                reportVM.isFiltered =
                    false; navController.navigate(Screen.AdminReport.route); reportVM.finalOrderStatusValue.value =
                "Tất cả"
            }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                Text(text = "Tạo báo cáo mặc định", style = mediumTitle)
            }
            if (!reportVM.isFiltered) {
                orders = remember { orderVM.getAllOrders() }
                for (order in orders) {
                    reportVM.getProductCountByStatus(order)
                }
            }
        }

    } else {

        if (reportVM.isFiltered) {
            orders = reportVM.filteredOrders
            for (order in reportVM.filteredOrders) {
                reportVM.getProductCountByStatus(order)
            }
            when (reportVM.finalOrderStatusValue.value.lowercase()) {
                "Đang xử lý" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[0].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[0].amount
                }
                "Đang giao" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[1].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[1].amount
                }
                "Đã giao" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[2].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[2].amount
                }
            }
        } else {
            orders = remember { orderVM.getAllOrders() }
            for (order in orders) {
                reportVM.getProductCountByStatus(order)
            }
        }

        AdminReports(navController = navController, products, orders)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReports(
    navController: NavHostController, allProducts: List<Product>, orders: List<Order>,
    @SuppressLint("ContextCastToActivity") reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
) {
    Scaffold(
        topBar = {
            TopBar("Báo cáo", actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.FilterReport.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Localized description"
                    )
                }
            })
        },
        containerColor = Color(0xFFEAD9B5),
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                var isCollapsed by remember { mutableStateOf(true) }

                Text(

                    text = "${reportVM.totalProductsCount.value} sản phẩm",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    style = header,
                    color = Color(0xFF4B1E1E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "Ngày bắt đầu: ${reportVM.startDate.value}",
                                softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle,
                        color = Color(0xFF6D4C41)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Ngày kết thúc: ${reportVM.endDate.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle,
                        color = Color(0xFF6D4C41)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "Tổng giá trị $${round(reportVM.totalSales)}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle,
                        color = Color(0xFF4B1E1E)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Trạng thái: ${reportVM.finalOrderStatusValue.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle,
                        color = Color(0xFF4B1E1E)
                    )
                }
                if (!isCollapsed) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Đang xử lí", style = mediumTitle, color = Color(0xFF4B1E1E))
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[0].count} sản phẩm",
                            style = smallTitle,
                            color = Color(0xFF6D4C41)
                        )
                        Text(
                            text = "Tổng tiền $${round(reportVM.totalProductsByStatus[0].amount)}",
                            style = smallTitle,
                            color = Color(0xFF6D4C41)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Đã giao", style = mediumTitle, color = Color(0xFF4B1E1E))
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[1].count} sản phẩm",
                            style = smallTitle,
                            color = Color(0xFF6D4C41)
                        )
                        Text(
                            text = "Tổng tiền $${round(reportVM.totalProductsByStatus[1].amount)}",
                            style = smallTitle,
                            color = Color(0xFF6D4C41)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Đã nhận", style = mediumTitle, color = Color(0xFF4B1E1E))
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[2].count} sản phẩm",
                            style = smallTitle,
                            color = Color(0xFF6D4C41)
                        )
                        Text(
                            text = "Tổng tiền $${round(reportVM.totalProductsByStatus[2].amount)}",
                            style = smallTitle,
                            color = Color(0xFF6D4C41)
                        )
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                if (reportVM.finalOrderStatusValue.value.lowercase() == "all") TextButton(onClick = {
                    isCollapsed = !isCollapsed
                }) {
                    Text(text = if (!isCollapsed) "Thu gọn" else "Xem chi tiết")
                }

                Divider(color = Color(0xFF6D4C41), thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    contentPadding = PaddingValues(all = 4.dp)
                ) {
                    items(items = orders) { order ->
                        AdminReportCardInit(order = order, allProducts)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                Divider(color = Color(0xFF6D4C41), thickness = 1.dp)
            }
        },
        bottomBar = { AdminBottomBar(navController = navController) } //navController = navController
    )
}