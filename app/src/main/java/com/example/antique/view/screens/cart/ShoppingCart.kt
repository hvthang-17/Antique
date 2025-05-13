package com.example.antique.view.screens.cart

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.CartViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.antique.R
import com.example.antique.view.components.AdminBottomBar

@SuppressLint("ContextCastToActivity")
@ExperimentalMaterial3Api
@Composable
fun ShoppingCart(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    viewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    viewModel.setUser(
        viewModel<AppViewModel>(LocalContext.current as ComponentActivity).getCurrentUser(),
    )

    Scaffold(topBar = { TopBar("Giỏ hàng", { navController.popBackStack() }) },
        containerColor = Color(0xFFF8EBCB),
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val userCart = viewModel.userCart.value

                if (userCart.isEmpty()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.empty_cart),
                            contentDescription = "Empty cart image",
                            Modifier.size(250.dp)
                        )
                        Text(
                            "Giỏ hàng của bạn đang trống",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4B1E1E)
                        )
                        Text(
                            "Bạn chưa thêm sản phẩm nào vào giỏ hàng.",
                            Modifier.padding(bottom = 10.dp),
                            color = Color(0xFF4B1E1E)
                        )
                        Button(
                            onClick = { navController.navigate(Screen.Home.route) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6D4C41),
                                contentColor = Color(0xFFF8EBCB)
                            )
                        ) {
                            Text("Xem sản phẩm")
                        }
                    }
                } else {
                    LazyColumn(Modifier.weight(4f)) {
                        items(items = userCart, key = { userCart -> userCart.cart.id }) {
                            CartItemCard(
                                it, viewModel
                            ) { navController.navigate("product/${it.product.id}") }
                        }
                    }

                    Divider(Modifier.padding(top = 10.dp), color = Color(0xFF4B1E1E))

                    Column(
                        Modifier.padding(start = 10.dp, end = 10.dp)
                    ) {
                        val totalPrice = viewModel.totalPrice.value
                        val couponCode by remember { viewModel.couponCode }
                        val couponMessage by remember { viewModel.couponMessage }

                        OutlinedTextField(
                            value = couponCode,
                            onValueChange = { viewModel.couponCode.value = it },
                            label = { Text("Nhập mã giảm giá") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6D4C41),
                                unfocusedBorderColor = Color(0xFF8D6E63),
                                cursorColor = Color(0xFF6D4C41)
                            )
                        )

                        Button(
                            onClick = { viewModel.applyCouponCode() },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6D4C41),
                                contentColor = Color(0xFFF8EBCB)
                            )
                        ) {
                            Text("Áp dụng mã")
                        }

                        if (couponMessage.isNotBlank()) {
                            Text(
                                text = couponMessage,
                                color = if (couponMessage.contains("thành công")) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng cộng:", fontSize = 16.sp, color = Color(0xFF4B1E1E))
                            Text("$$totalPrice", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF4B1E1E))
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Giảm giá hội viên:", fontSize = 16.sp, color = Color(0xFF4B1E1E))
                            Text(
                                "${viewModel.userDiscount}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF4B1E1E)
                            )
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Giảm giá mã giảm giá:", fontSize = 16.sp, color = Color(0xFF4B1E1E))
                            Text("${viewModel.couponDiscount}%", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF4B1E1E))
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng thanh toán:", fontSize = 16.sp, color = Color(0xFF4B1E1E))
                            Text(
                                "$${viewModel.grandTotal.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF4B1E1E)
                            )
                        }

                        Button(
                            onClick = { navController.navigate(Screen.SelectAddress.route) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6D4C41),
                                contentColor = Color(0xFFF8EBCB)
                            )
                        ) {
                            Text("Thanh toán", fontSize = 18.sp)
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        })
}