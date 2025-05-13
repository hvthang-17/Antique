package com.example.antique.view.screens.placeorder

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.R
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.PlaceOrderViewModel

@SuppressLint("ContextCastToActivity")
@ExperimentalMaterial3Api
@Composable
fun SelectPayment(navController: NavHostController) {
    val parchment = Color(0xFFF8EBCB)
    Scaffold(
        containerColor = parchment,
        topBar = { TopBar("Chọn phương thức thanh toán", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val navToSummary = { navController.navigate(Screen.OrderSummary.route) }

                PaymentOptionCard(
                    optionText = "Thanh toán khi nhận hàng",
                    iconID = R.drawable.cash,
                    navToSummary = navToSummary
                )

                PaymentOptionCard(
                    optionText = "Thẻ tín dụng/Ghi nợ",
                    iconID = R.drawable.creditcard,
                    navToSummary = navToSummary
                )

                PaymentOptionCard(
                    optionText = "PayPal", iconID = R.drawable.paypal, navToSummary = navToSummary
                )

                Divider(
                    Modifier.padding(top = 10.dp),
                    color = Color(0xFF6D4C41).copy(alpha = 0.4f)
                )
            }
        },
        bottomBar = {
            if (viewModel<AppViewModel>(LocalContext.current as ComponentActivity).isAdmin) AdminBottomBar(
                navController = navController
            )
            else UserBottomBar(navController = navController)
        })
}

@ExperimentalMaterial3Api
@Composable
fun PaymentOptionCard(
    optionText: String,
    iconID: Int,
    navToSummary: () -> Unit,
    @SuppressLint("ContextCastToActivity") placeOrderViewModel: PlaceOrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val woodBrown = Color(0xFF6D4C41)
    val parchment = Color(0xFFF8EBCB)
    Card(
        onClick = {
            placeOrderViewModel.selectedPayment = optionText
            navToSummary()
        },
        Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = parchment),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconID),
                contentDescription = "Payment option icon",
                Modifier
                    .size(50.dp)
                    .padding(horizontal = 10.dp)
            )
            Text(
                text = optionText,
                color = woodBrown,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}