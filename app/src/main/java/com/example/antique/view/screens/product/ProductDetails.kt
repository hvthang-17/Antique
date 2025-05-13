package com.example.antique.view.screens.product

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.antique.R
import com.example.antique.model.remote.entity.Review
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.ProductViewModel

@SuppressLint("ContextCastToActivity")
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ProductDetails(
    navController: NavHostController,
    productId: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    viewModel: ProductViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val currentUserId = appViewModel.getCurrentUserId()
    viewModel.setCurrentProduct(productId)

    val product = viewModel.product

    val reviews = product.value.reviews
    val avgRating = viewModel.getProductAvgRating(reviews)
    val reviewCount = reviews.size

    val userBought = viewModel.userBoughtItem(currentUserId, productId)

    Scaffold(topBar = { TopBar(navBack = { navController.popBackStack() }) }, content = { padding ->
        Column(
            Modifier
                .background(Color(0xFFF8EBCB))
                .padding(padding)
                .padding(20.dp)
                .fillMaxHeight()
        ) {
            var titleFontSize = 25.sp
            if (product.value.title.length > 55) titleFontSize = 22.sp
            Text(product.value.title, fontWeight = FontWeight.Bold, fontSize = titleFontSize, color = Color(0xFF4B1E1E))

            if (reviewCount != 0) {
                Row(Modifier.padding(bottom = 10.dp)) {
                    Text("⭐$avgRating", fontWeight = FontWeight.Bold, color = Color(0xFF6D4C41))
                    Text(" ($reviewCount ${if (reviewCount == 1) "Đánh giá" else "Các đánh giá"})")
                }
            }

            LazyColumn(
                Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = product.value.image),
                            contentDescription = "Product image",
                            modifier = Modifier
                                .size(360.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }
                }

                item {
                    Text(
                        product.value.description,
                        color = Color(0xFF4B1E1E),
                        lineHeight = 20.sp,
                        fontSize = 15.sp
                    )
                }

                item { Divider(thickness = 1.dp, color = Color(0xFF8B5E3C)) }

                item() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Các đánh giá", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4B1E1E))
                        if (userBought) {
                            IconButton(onClick = {
                                navController.navigate("manageReview/${productId}")
                            }) {
                                Row() {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "Thêm đánh giá",
                                        tint = Color(0xFF8B5E3C)
                                    )
                                }
                            }
                        }
                    }
                }

                if (reviewCount != 0) items(items = reviews) { ReviewCard(it) }
                else item() { Text("Sản phẩm này chưa có đánh giá nào.") }
            }

            val openDialog = remember { mutableStateOf(false) }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$${product.value.price}", fontWeight = FontWeight.Bold, fontSize = 25.sp,  color = Color(0xFF8B5E3C))

                if (product.value.stock > 0) {
                    Button(
                        onClick = {
                            viewModel.addToCart(currentUserId, productId)
                            openDialog.value = true
                        },
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B5E3C),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Thêm vào giỏ hàng", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = "Hết hàng",
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
            }

            if (openDialog.value) {
                AlertDialog(
                    containerColor = Color(0xFFF8EBCB),
                    onDismissRequest = { openDialog.value = false },
                    title = { Text("Đã thêm vào giỏ hàng", color = Color(0xFF4B1E1E)) },
                    text = {
                        Column() {
                            Text(
                                "Sản phẩm đã được thêm vào giỏ hàng thành công.",
                                Modifier.padding(top = 10.dp),
                                color = Color(0xFF4B1E1E)
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5E3C)),
                            onClick = {
                                openDialog.value = false
                                navController.navigate(Screen.Cart.route)
                            }
                        ) {
                            Text("Đến giỏ hàng", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D4C41)),
                            onClick = {
                                openDialog.value = false
                                navController.navigate(Screen.Home.route)
                            }
                        ) {
                            Text("Tiếp tục mua", color = Color.White)
                        }
                    })
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    }
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ReviewCard(review: Review) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Review user profile picture",
                        Modifier
                            .size(25.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )

                    Text(
                        text = review.date,
                        Modifier.padding(start = 5.dp, end = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text("⭐".repeat(review.stars))
            }
            Text(review.title, fontWeight = FontWeight.Bold)
            Text(review.details)
        }
    }
}
