package com.example.antique.view.screens.admin.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.antique.model.remote.entity.Order
import com.example.antique.view.theme.largeTitle
import com.example.antique.view.theme.smallTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestOrderCard(navController: NavController, order: Order) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .height(92.dp)
            .fillMaxWidth(),
        onClick = { navController.navigate("orderDetails/${order.id}") },
        //colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceTint),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {

        Column(
            Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Đơn hàng# ${order.id}", style = largeTitle
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Tổng cộng: ${order.total}$", style = smallTitle)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(text = "Ngày: ${order.date}", style = smallTitle)
                Text(
                    text = "Trạng thái: ${order.status}", textAlign = TextAlign.End, style = smallTitle
                )
            }
        }
    }
}


