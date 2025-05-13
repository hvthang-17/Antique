package com.example.antique.view.screens.placeorder

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.model.remote.entity.CartWithProduct
import com.example.antique.model.remote.entity.Address
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.SuccessDialog
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.CartViewModel
import com.example.antique.viewmodel.PlaceOrderViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ContextCastToActivity")
@ExperimentalMaterial3Api
@Composable
fun OrderSummary(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    placeOrderViewModel: PlaceOrderViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    val parchment = Color(0xFFF8EBCB)
    val woodBrown = Color(0xFF4B1E1E)

    Scaffold(
        containerColor = parchment,
        topBar = {
            TopBar("Tóm tắt đơn hàng", { navController.popBackStack() })
        },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val userCart = cartViewModel.userCart.value
                val totalPrice = cartViewModel.grandTotal.value

                Column(Modifier.weight(5f, true)) {
                    SummaryPageSubtitleText("CÁC MẶT HÀNG", woodBrown)

                    LazyColumn(
                        Modifier
                            .weight(1f)
                            .padding(bottom = 5.dp)
                    ) {
                        items(items = userCart, key = { it.cart.id }) {
                            SummaryPageItemRow(it, woodBrown, parchment)
                        }
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "TỔNG CỘNG:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = woodBrown
                        )
                        Text(
                            "$$totalPrice",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = woodBrown
                        )
                    }

                    SummaryPageDivider(woodBrown)
                }

                Column(Modifier.weight(4f)) {
                    SummaryPageSubtitleText("GIAO ĐẾN", woodBrown)
                    SummaryPageAddressDetails(placeOrderViewModel.selectedAddress, parchment, woodBrown)
                    SummaryPageDivider(woodBrown)
                }

                Column(Modifier.weight(1f)) {
                    SummaryPageSubtitleText("PHƯƠNG THỨC THANH TOÁN", woodBrown)
                    Text(placeOrderViewModel.selectedPayment, color = Color.DarkGray)
                    SummaryPageDivider(woodBrown)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val openDialog = remember { mutableStateOf(false) }

                    Button(
                        onClick = {
                            placeOrderViewModel.placeOrder(userCart, totalPrice)
                            openDialog.value = true
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D4037),
                            contentColor = Color(0xFFF5E0B7)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Đặt hàng",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                SuccessDialog(
                        openDialog,
                        "Đơn hàng đã được xác nhận",
                        "Cảm ơn bạn đã mua sắm cùng chúng tôi!",
                        "Về trang chủ"
                    ) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
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
fun SummaryPageSubtitleText(text: String, color: Color) {
    Text(
        text,
        modifier = Modifier.padding(bottom = 8.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
fun SummaryPageItemRow(cartItem: CartWithProduct, textColor: Color, cardColor: Color) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cartItem.product.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textColor
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$${cartItem.product.price}", color = textColor)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "x ${cartItem.cart.quantity}", color = textColor)
                }
            }
        }
    }
}

@Composable
fun SummaryPageAddressDetails(address: Address, bgColor: Color, textColor: Color) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(address.name, fontWeight = FontWeight.Bold, color = textColor)
            Text("SĐT: ${address.phone}", color = textColor)
            Text("Tên đường: ${address.street}", color = textColor)
            Text("Phường/Xã: ${address.ward}", color = textColor)
            Text("Quận/Huyện ${address.district}", color = textColor)
            Text("Tỉnh/Thành phố: ${address.city}", color = textColor)
            Text("Mã bưu điện: ${address.poBox}", color = textColor)
        }
    }
}

@Composable
fun SummaryPageDivider(color: Color) {
    Divider(
        Modifier.padding(vertical = 10.dp),
        color = color.copy(alpha = 0.5f),
        thickness = 1.dp
    )
}
