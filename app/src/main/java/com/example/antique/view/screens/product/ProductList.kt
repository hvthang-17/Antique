package com.example.antique.view.screens.product

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.model.remote.entity.Product
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.HomeViewModel
import com.example.antique.viewmodel.ProductViewModel
import com.example.antique.viewmodel.admin.ManageProductVM

@SuppressLint("ContextCastToActivity")
@ExperimentalMaterial3Api
@Composable
fun ProductList(
    navController: NavHostController,
    title: String?,
    param: String?,
    type: String?,
    cid: String?,
    filterStatus: Int?,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    homeViewModel: HomeViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val productVM = viewModel<ProductViewModel>(LocalContext.current as ComponentActivity)
    val manageProductVM = viewModel<ManageProductVM>(LocalContext.current as ComponentActivity)

    val navRoute = remember {
        mutableStateOf("FilterProducts/-1/$title")
    }

    val productFlowList = productVM.products.value
    var products: List<Product>? = emptyList()

    if (param != "") products = homeViewModel.getLikeProducts(param!!)
    else if (filterStatus != 1) { // highlight or category --- type != "Filtered"
        when (type) {
            "Highlight" -> {
                products = homeViewModel.getProductsByHighlights(title ?: "")
            }
            "Category" -> {
                navRoute.value = "FilterProducts/$cid/$title"
                products = homeViewModel.getCategoryProducts(cid!!)
            }
            "Admin" -> {
                homeViewModel.actionType = ""
                navRoute.value = "manageProduct/Thêm"
                products = productFlowList
            }
        }
    } else { // "Filtered"
        navRoute.value = "FilterProducts/$cid/$title" ///-1/Normal";
        products = homeViewModel.getProductList()
    }

    Scaffold(topBar = {
        TopBar(title!!, { navController.popBackStack() }, actions = {
            IconButton(onClick = {
                if (type == "Admin") {
                    manageProductVM.resetCurrentProduct()
                    navController.navigate(navRoute.value)

                } else {
                    navController.navigate(navRoute.value)
                }
            }) {
                var imageVector = Icons.Filled.FilterList
                if (type == "Admin") imageVector = Icons.Filled.Add

                Icon(
                    imageVector = imageVector, contentDescription = "Localized description"
                )
            }
        })
    }, content = { padding ->
        Column(
            modifier = Modifier
                .background(Color(0xFFF8EBCB))
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val addedCartToast =
                Toast.makeText(LocalContext.current, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT)

            if (products?.isEmpty() == true) {
                var navRoute = "FilterProducts/${if (cid == "") "-1" else cid}/$title"
                if (filterStatus != 1)
                    navRoute = "home"
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Không tìm thấy!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Button(onClick = { navController.navigate(navRoute) }) {
                        Text("Thử lại")
                    }
                }


            } else {
                LazyColumn(
                    contentPadding = PaddingValues(all = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(items = products!!, key = { product -> product.id }) { product ->
                        ProductListCard(
                            product,
                            addedCartToast,
                            navController
                        )
                    }
                }
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    })
}
