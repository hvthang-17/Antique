package com.example.antique.view.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.view.components.AdminBottomBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.FilterViewModel
import com.example.antique.viewmodel.HomeViewModel
import com.example.antique.viewmodel.ProductViewModel

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavHostController,
    @SuppressLint("ContextCastToActivity") appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") homeViewModel: HomeViewModel = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") productVM: ProductViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val filterVM = viewModel<FilterViewModel>(LocalContext.current as ComponentActivity)
    val categories = remember { homeViewModel.getAllCategories() }

    productVM.getProducts()

    Scaffold(topBar = { HomeTopBar(homeViewModel, navController) }, content = { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                contentPadding = PaddingValues(all = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(0.5.dp),
                state = rememberLazyListState()
            ) {
                items(items = categories!!, key = { category -> category.id }) { category ->
                    TextButton(onClick = {
                        Log.d("cid value inside home", category.name)
                        filterVM.setCurrentIndex(category.cid.toString()); navController.navigate( //until cid gets fixed
                        "ProductList/${category.name}/Category/${category.cid}/0"
                    )
                        homeViewModel.actionType = "category"
                    }) { Text(text = "${category.name}") }
                }
            }

            Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Gray)

            LazyColumn(
                contentPadding = PaddingValues(all = 10.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                state = rememberLazyListState()
            ) {
                items(
                    items = homeViewModel.highlightList,
                    key = { highlight -> highlight }) { highlight ->
                    ProductHighlights(highlight = highlight, navController)
                }
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    })
}



