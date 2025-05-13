package com.example.antique.view.screens.orders

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.antique.model.remote.entity.FullOrderDetail
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.OrderStatus
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.DropDownMenu
import com.example.antique.view.components.OrderStatusDropDownMenu
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.theme.largeTitle
import com.example.antique.view.theme.smallCaption
import com.example.antique.view.theme.smallTitle
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.OrderDetailsViewModel
import com.example.antique.viewmodel.OrderViewModel
import com.example.antique.viewmodel.admin.ReportVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@SuppressLint("ContextCastToActivity", "CoroutineCreationDuringComposition")
@Composable
fun OrderDetails(
    navController: NavHostController,
    order_id: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    orderDetailsViewModel: OrderDetailsViewModel = viewModel(LocalContext.current as ComponentActivity),
    reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    val order = orderDetailsViewModel.getOrderById(order_id)
    val orderItemsWithProduct = orderDetailsViewModel.orderItemProducts
    orderViewModel.orderStatus.value =
        OrderStatus.values().find { it.code == order.status } ?: OrderStatus.Processing
    val context = LocalContext.current
    val textModifier = Modifier
    Scaffold(
        topBar = { TopBar("Chi tiết đơn hàng", { navController.popBackStack() }) },
        containerColor = Color(0xFFF8EBCB),
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(0.30f)
                ) {
                    val labelColor = Color(0xFF6D4C41)
                    val valueColor = Color(0xFF4B1E1E)

                    if (!appViewModel.isAdmin)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(text = "Trạng thái", color = labelColor, style = smallCaption)
                            Text(text = order.status, color = valueColor, style = smallTitle)
                        }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Ngày đặt hàng ", color = labelColor, style = smallCaption)
                        Text(order.date, color = valueColor, style = smallTitle)
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Số lượng mặt hàng ", color = labelColor, style = smallCaption)
                        Text("${order.items.size}", color = valueColor, style = smallTitle)
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Tổng cộng ", color = labelColor, style = smallCaption)
                        Text("$${order.total}", color = valueColor, style = smallTitle)
                    }
                }
                if (appViewModel.isAdmin) {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Trạng thái đơn hàng",
                            style = largeTitle,
                            color = Color(0xFF4B1E1E)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OrderStatusDropDownMenu(
                            orderViewModel = orderViewModel,
                            options = listOf(
                                OrderStatus.Processing,
                                OrderStatus.Shipped,
                                OrderStatus.Delivered
                            )
                        )

//                        if (orderViewModel.isOrderUpdated.value) {
//                            orderViewModel.updateStatus(order, orderViewModel.orderStatus.value)
//                            orderViewModel.isOrderUpdated.value = false
//                            Toast.makeText(
//                                context,
//                                "Trạng thái đơn hàng đã được cập nhật thành ${orderViewModel.orderStatus.value.code}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
                        if (orderViewModel.isOrderUpdated.value) {
                            val updatedStatus = orderViewModel.orderStatus.value
                            orderViewModel.updateStatus(order, updatedStatus)

                            // Gửi email khi có trạng thái mới
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val email = orderViewModel.getUserById(order.uid)
                                    email?.let {
                                        orderViewModel.sendStatusUpdateEmail(it, updatedStatus.code)
                                    }
                                } catch (e: Exception) {
                                    Log.e("SendGrid", "Lỗi lấy email và gửi: ${e.message}")
                                }
                            }

                            orderViewModel.isOrderUpdated.value = false

                            Toast.makeText(
                                context,
                                "Trạng thái đơn hàng đã được cập nhật thành ${updatedStatus.code}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
                Divider(Modifier.padding(vertical = 20.dp), color = Color(0xFF6D4C41))

                LazyColumn(
                    Modifier.weight(1f)
                ) {
                    items(
                        items = orderItemsWithProduct.value
                    ) { orderItem ->
                        ItemCard(
                            navController,
                            item = orderItem,
                            order,
                            appViewModel.getCurrentUserId()
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    navController: NavHostController,
    item: FullOrderDetail,
    order: Order,
    currentUserId: String
) {
    Card(
        onClick = {
            item.product?.let { navController.navigate("product/${it.id}") }
        },
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDAD7CD)),
        modifier = Modifier
            .height(160.dp)
            .padding(top = 10.dp, bottom = 10.dp)
    ) {

        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            item.product?.let {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = it.image),
                        contentDescription = null,
                        modifier = Modifier
                            .height(150.dp)
                            .width(100.dp)
                            .padding(5.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Column(
                Modifier
                    .padding(start = 20.dp, bottom = 5.dp, top = 5.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.padding(bottom = 5.dp)) {
                    item.product?.let {
                        Text(
                            text = it.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF4B1E1E),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    item.orderItem?.let {
                        Text("Số lượng: ${it.quantity}", color = Color(0xFF6D4C41))
                        Text("Giá: $${it.price}", color = Color(0xFF6D4C41))
                    }

                    if (order.status == "Đã giao" && order.uid == currentUserId) {
                        Text(
                            text = "Đánh giá",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Blue,
                            modifier = Modifier.clickable {
                                navController.navigate("manageReview/${item.product.id}")
                            })
                    }
                }
            }
        }
    }
}