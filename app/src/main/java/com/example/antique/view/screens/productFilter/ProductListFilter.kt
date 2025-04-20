package com.example.antique.view.screens.productFilter

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.model.remote.entity.Category
import com.example.antique.model.remote.entity.Product
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.view.screens.product.productTypes
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.HomeViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun ProductListFilterInit(
    navController: NavHostController,
    category_id: String? ,
    productType: String?,
) {
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)
    val categoryList = remember { homeVM.getAllCategories() }
    Log.d("category", "$categoryList")

    ProductListFilter(navController, category_id, productType, categoryList, false)
}

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("ContextCastToActivity")
fun ProductListFilter(
    navController: NavHostController,
    category_id: String? ,
    productType: String?,
    categoryList: List<Category>,
    isCategoryChip: Boolean,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    Log.d("category id", category_id!!)
    val scope = rememberCoroutineScope()
    val skipHalfExpanded by remember { mutableStateOf(false) }
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = skipHalfExpanded
    )
    Log.d("type", "$productType")
    Log.d("cid", category_id)
    var navRoute = remember {
        "ProductList/${productType}/$productType/$category_id/1"
    }

    val result: List<Product>? = productTypes(type = productType, cid = category_id)
    Log.d("type", "$result")
    result?.let {
        viewModel<HomeViewModel>(LocalContext.current as ComponentActivity).setProductList(it)
    }

    ModalBottomSheetLayout(sheetState = state, sheetContent = {
        PriceFilterBottomSheet()
    }) {

        Scaffold(topBar = {
            TopBar("Filter $productType", { navController.popBackStack() }) {
                TextButton(onClick = {
                    if (category_id == "-1" && categoryList.contains(Category(name = productType!!))) {
                        navRoute = "ProductList/All products/All products/$category_id/1"
                    }
                    navController.navigate(navRoute) {
                        popUpTo("home")

                    }
                }) {

                    Text(
                        text = "Áp dụng",
                        Modifier.padding(end = 5.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${result?.size}",
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                    )
                    Text(
                        text = "Kết quả tìm kiếm",
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                    )
                }

                //CategoryFilterComponent(categoryList = categoryList)
                PriceFilterComponent(scope = scope, state = state)
                ReviewFilterComponent()
            }
        }, bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        })
    }
}


