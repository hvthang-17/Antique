package com.example.antique.view.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.antique.model.remote.entity.CartWithProduct
import com.example.antique.viewmodel.CartViewModel

@ExperimentalMaterial3Api
@Composable
fun CartItemCard(
    cartItem: CartWithProduct,
    viewModel: CartViewModel,
    navToProductPage: () -> Unit
) {
    val openRemoveCartItemDialog = remember { mutableStateOf(false) }

    Card(
        onClick = { navToProductPage() },
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 5.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            FilledIconButton(
                onClick = { openRemoveCartItemDialog.value = true },
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove item button."
                )
            }
        }
        Row(
            Modifier
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                .height(IntrinsicSize.Min)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = cardColors(containerColor = Color.White)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = cartItem.product.image),
                    contentDescription = "Cart Item Image",
                    Modifier
                        .size(80.dp)
                        .padding(5.dp)
                )
            }
            Column(
                Modifier
                    .padding(start = 10.dp, bottom = 5.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = cartItem.product.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "$${cartItem.product.price}")

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        FilledIconButton(
                            onClick = {
                                if (cartItem.cart.quantity == 1) {
                                    openRemoveCartItemDialog.value = true
                                } else
                                    viewModel.updateQty(cartItem, -1)

                            },
                            modifier = Modifier.size(19.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Decrease item quantity button"
                            )
                        }

                        Text(text = "${cartItem.cart.quantity}")

                        FilledIconButton(
                            onClick = {
                                viewModel.updateQty(cartItem, 1)
                            },
                            modifier = Modifier.size(19.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Increase item quantity button"
                            )
                        }
                    }
                }
            }
        }
    }

    if (openRemoveCartItemDialog.value) {
        AlertDialog(
            onDismissRequest = { openRemoveCartItemDialog.value = false },
            title = {
                Text(text = "Xóa sản phẩm")
            },
            text = {
                Text("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openRemoveCartItemDialog.value = false
                        viewModel.deleteCartItem(cartItem.cart)
                    }
                ) {
                    Text("Có")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openRemoveCartItemDialog.value = false
                    }) {
                    Text("Hủy")
                }
            }
        )
    }
}