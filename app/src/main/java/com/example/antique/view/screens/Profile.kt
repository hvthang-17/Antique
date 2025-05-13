package com.example.antique.view.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
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
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment

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
                .background(Color(0xFFF8EBCB))
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD9C789))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Ảnh đại diện",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .border(2.dp, Color(0xFF8C6A3F), RoundedCornerShape(15.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${currentUser?.firstName} ${currentUser?.lastName}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3F2C1B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text("Loại tài khoản: ", color = Color(0xFF6C4B2A))
                            Text(
                                "${currentUser?.type}",
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF3F2C1B)
                            )
                        }
                    }
                }
            }

            Divider(thickness = 1.dp, color = Color(0xFFB49A75))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD9C789)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileMenuItem("Đơn hàng của tôi", onClick = {
                        navController.navigate(Screen.Orders.route)
                    })

                    ProfileMenuItem("Địa chỉ của tôi", onClick = {
                        navController.navigate(Screen.MyAddresses.route)
                    })
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = "Đăng xuất",
                tint = Color(0xFF8C3D2E),
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        appViewModel.setCurrentUserId("")
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
            )

        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(
            navController = navController
        )
        else UserBottomBar(navController = navController)
    })
}

@Composable
fun ProfileMenuItem(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF3F2C1B)
    )
}
