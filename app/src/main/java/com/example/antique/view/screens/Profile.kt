package com.example.antique.view.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.R
import com.example.antique.model.remote.entity.User
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.navigation.Screen
import com.example.antique.viewmodel.AppViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Profile(
    navController: NavHostController,
    @SuppressLint("ContextCastToActivity") appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val profileMenuItemFontSize = 20.sp
    val currentUser: User? = appViewModel.getCurrentUser()

    Scaffold(topBar = { TopBar("Hồ sơ của tôi") }, content = { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Ảnh đại diện",
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(15.dp))
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Column() {
                    Text(
                        text = "${currentUser?.firstName} ${currentUser?.lastName}",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row() {
                        Text("Loại tài khoản: ")
                        Text("${currentUser?.type}", fontWeight = FontWeight.Bold)
                    }
                }

            }
            Divider()
            Text(
                "Đơn hàng của tôi",
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(route = Screen.Orders.route)
                    }, fontSize = profileMenuItemFontSize
            )
            Text(
                "Địa chỉ của tôi",
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(route = Screen.MyAddresses.route)
                    }, fontSize = profileMenuItemFontSize
            )
            Box(
                Modifier.weight(1f), contentAlignment = BottomEnd
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text(
                        "Đăng xuất",
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                appViewModel.setCurrentUserId("")
                                navController.navigate(route = Screen.Login.route) {
                                    popUpTo(0)
                                }
                            }, fontSize = profileMenuItemFontSize
                    )
                }
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(
            navController = navController
        )
        else UserBottomBar(navController = navController)
    })
}