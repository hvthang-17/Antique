package com.example.antique.view.screens.admin.report

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antique.model.remote.entity.Order
import com.example.antique.model.remote.entity.Product
import com.example.antique.view.theme.largeTitle
import com.example.antique.view.theme.mediumTitle
import com.example.antique.view.theme.smallCaption
import com.example.antique.view.theme.smallTitle
import com.example.antique.viewmodel.admin.ReportVM

@Composable
fun AdminReportCardInit(order: Order, allProducts: List<Product>) {
    AdminReportCard(order, allProducts)
}

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportCard(order: Order, allProducts: List<Product>) {
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)

    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        onClick = {},
        shape = CardDefaults.outlinedShape
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(text = "Order# ${order.id}", style = largeTitle)
            Text(text = "Status: ${order.status}", style = mediumTitle)

            Spacer(Modifier.height(5.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = order.date, style = smallTitle)
                Text(text = "Total: $${order.total}", style = smallTitle)
            }

            Divider(color = MaterialTheme.colorScheme.inversePrimary)
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Product xQuantity", style = smallCaption)
                Text(text = "Price", style = smallCaption)
            }

            Spacer(modifier = Modifier.height(10.dp))

            for (item in order.items.orEmpty()) {
                val product = reportVM.getProductDetails(item.pid, allProducts)

                if (product != null) {
                    val productTitleShort = product.title
                        .split(" ")
                        .take(3)
                        .joinToString(" ")

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Text(text = productTitleShort, style = smallTitle)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "x${item.quantity}", style = smallTitle)
                        }
                        Text(text = "$${product.price}", style = smallTitle)
                    }
                }
            }
        }
    }
}
