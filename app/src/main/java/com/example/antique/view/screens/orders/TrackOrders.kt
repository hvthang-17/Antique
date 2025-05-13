package com.example.antique.view.screens.orders

import android.annotation.SuppressLint
import android.graphics.Color.parseColor
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.R
import com.example.antique.model.remote.entity.Order
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.theme.largeTitle
import com.example.antique.view.theme.mediumCaption
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.OrderViewModel

@ExperimentalMaterial3Api
@Composable
fun TrackOrders(
    navController: NavHostController,
    @SuppressLint("ContextCastToActivity") appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    orderViewModel.setUserId(
        appViewModel.getCurrentUserId()
    )

    val orders = orderViewModel.orders

    Scaffold(
        containerColor = Color(0xFFF8EBCB),
        topBar = {
        TopBar(title = "Theo dõi đơn hàng", { navController.popBackStack() })
    }, content = { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .padding(20.dp)
        ) {
            items(items = orders.value) { order ->
                OrderCard(order = order, navController)
            }
        }

    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    })
}

@ExperimentalMaterial3Api
@Composable
fun OrderCard(order: Order, navController: NavHostController) {
    Card(
        onClick = {
            navController.navigate("orderDetails/${order.id}")
        },

        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 15.dp)
            .height(175.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDAD7CD))
    ) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxHeight()
        ) {
            Row() {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ID", style = largeTitle, color = Color(0xFF6D4C41))
                        Text(order.id, style = largeTitle, color = Color(0xFF4B1E1E))
                    }
                    Divider(color = Color(0xFF6D4C41))
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = order.date, style = largeTitle,  color = Color(0xFF4B1E1E)
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Trạng thái:  ${order.status}", style = mediumCaption, color = Color(0xFF6D4C41))
                        Text("Tổng cộng: $${order.total}", style = mediumCaption, color = Color(0xFF6D4C41))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                StepBar(doneStatus = order.status)
            }
        }
    }
}

@Composable
fun StepBar(doneStatus: String) {
    val stepIsComplete: Int = when (doneStatus) {
        "Đang xử lý" -> 1
        "Đang giao" -> 2
        "Đã giao" -> 3
        else -> 1 // fallback nếu lỡ sai
    }

    Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
        Step("Đang xử lý", 1, stepIsComplete)
        Step("Đã giao", 2, stepIsComplete)
        Step("Đã nhận hàng", 3, stepIsComplete)
    }
}

@Composable
fun Step(status: String, stepNumber: Int, state: Int) {

    val innerCircleColor = if (state >= stepNumber) Color.hsl(120f, 1f, 0.3922f, 1f) else Color.Red

    val statusIcon = if (state != 0 && stepNumber <= state) {
        painterResource(id = R.drawable.check_circle)
    } else {
        painterResource(id = R.drawable.pending_circle2)
    }

    val icon: ImageVector = when (status) {
        "Đang xử lý" -> StatusIcons.PROCESSING
        "Đang giao" -> StatusIcons.SHIPPED
        "Đã giao" -> StatusIcons.DELIVERED
        else -> StatusIcons.PROCESSING
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = statusIcon,
            contentDescription = null,
            tint = innerCircleColor
        )
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }

}

object StatusIcons {
    val PROCESSING = Icons.Default.ShoppingBasket
    val SHIPPED = Icons.Default.LocalShipping
    val DELIVERED = Icons.Default.Inventory2
}

val String.color get() = Color(parseColor(this))