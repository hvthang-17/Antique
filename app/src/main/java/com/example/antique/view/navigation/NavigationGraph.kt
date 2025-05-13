package com.example.antique.view.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.antique.view.screens.*
import com.example.antique.view.screens.admin.dashboard.AdminDashboardInit
import com.example.antique.view.screens.admin.dashboard.DetailsPageInit
import com.example.antique.view.screens.admin.manageCategories.ManageCategoryInit
import com.example.antique.view.screens.admin.manageCoupons.ManageCouponInit
import com.example.antique.view.screens.admin.manageProducts.ManageProductInit
import com.example.antique.view.screens.admin.report.AdminReportFilter
import com.example.antique.view.screens.admin.report.AdminReportsInit
import com.example.antique.view.screens.cart.ShoppingCart
import com.example.antique.view.screens.category.CategoryList
import com.example.antique.view.screens.coupon.CouponList
import com.example.antique.view.screens.home.Home
import com.example.antique.view.screens.login.Forgot
import com.example.antique.view.screens.login.Login
import com.example.antique.view.screens.login.ResetPassword
import com.example.antique.view.screens.login.VerifyPassword
import com.example.antique.view.screens.orders.OrderDetails
import com.example.antique.view.screens.orders.TrackOrders
import com.example.antique.view.screens.placeorder.OrderSummary
import com.example.antique.view.screens.placeorder.SelectPayment
import com.example.antique.view.screens.product.ProductDetails
import com.example.antique.view.screens.product.ProductList
import com.example.antique.view.screens.productFilter.ProductListFilterInit

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ContextCastToActivity")
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            Login(onSuccessfulLogin = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }, onRegisterClick = {
                navController.navigate(Screen.Register.route)
            }, onForgotClick = {
                navController.navigate(Screen.Forgot.route)
            })
        }
        composable(route = Screen.Register.route) {
            Register(navToLogin = {
                navController.popBackStack()
                navController.navigate(Screen.Login.route) { launchSingleTop = true }
            })
        }
        composable(route = Screen.Forgot.route) {
            Forgot(navController,
                viewModel(LocalContext.current as ComponentActivity),
                onVerifyClick = { navController.navigate(route = Screen.VerifyPassword.route) })
        }
        composable(route = Screen.VerifyPassword.route) {
            VerifyPassword(
                navController, viewModel(LocalContext.current as ComponentActivity)
            )
        }
        composable(route = Screen.ResetPassword.route) {
            ResetPassword(
                navController, viewModel(LocalContext.current as ComponentActivity)
            )
        }
        composable(route = Screen.Home.route) {
            Home(navController)
        }

        composable(route = Screen.Cart.route) {
            ShoppingCart(navController)
        }
        composable(route = Screen.Profile.route) {
            Profile(navController)
        }

        composable(route = Screen.OrderSummary.route) {
            OrderSummary(navController)
        }

        composable(route = Screen.Orders.route) {
            TrackOrders(navController)
        }

        composable(
            route = Screen.OrderDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("id")
                ?.let { OrderDetails(navController, order_id = it) }
        }

        composable(
            route = Screen.ManageReview.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { it ->
            it.arguments?.getString("id")?.let { ManageReview(navController, productId = it) }
        }

        composable(
            route = "${Screen.ManageAddress.route}/{initScreen}/{addressId}", arguments = listOf(
                navArgument("initScreen") { type = NavType.StringType },
                navArgument("addressId") { type = NavType.StringType })
        ) { backStackEntry ->
            ManageAddress(
                navController,
                backStackEntry.arguments?.getString("initScreen"),
                backStackEntry.arguments?.getString("addressId") ?: ""
            )
        }

        composable(
            route = Screen.AdminProductList.route,
        ) {
            ProductList(navController, "Quản lý sản phẩm", "", "Admin", cid = "-1", 0)
        }

        composable(
            route = "${Screen.DashboardDetailsPage.route}/{pageType}/{title}", arguments = listOf(
                navArgument("pageType") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType })
        ) {
            val pageType = it.arguments?.getString("pageType")
            val title = it.arguments?.getString("title")
            DetailsPageInit(
                navController, pageType, title
            )
        }

        composable(
            route = Screen.Dashboard.route,
        ) {
            AdminDashboardInit(navController)
        }

        composable(
            route = Screen.AdminReport.route,
        ) {
            AdminReportsInit(navController)
        }
        composable(
            route = Screen.FilterReport.route,
        ) {
            AdminReportFilter(navController)
        }

        composable(
            route = Screen.ManageProduct.route, arguments = listOf(
                navArgument("actionType") { type = NavType.StringType },
            )
        ) {
            val actionType = it.arguments?.getString("actionType")
            ManageProductInit(navController = navController, actionType = actionType)
        }
        composable(
            route = "${Screen.ProductList.route}/{title}/{type}/{cid}/{filterStatus}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType },
                navArgument("cid") { type = NavType.StringType },
                navArgument("filterStatus") { type = NavType.IntType },
            )
        ) {
            val title = it.arguments?.getString("title")
            val type = it.arguments?.getString("type")
            val cid = it.arguments?.getString("cid")
            val filterStatus = it.arguments?.getInt("filterStatus")
            ProductList(navController, "$title", "", "$type", cid, filterStatus)
        }
        composable(route = Screen.MyAddresses.route) {
            Address(navController, "Quản lý")
        }

        composable(route = Screen.Payment.route) {
            SelectPayment(navController)
        }
        composable(route = Screen.SelectAddress.route) {
            Address(navController, "Chọn")
        }

        composable(
            route = Screen.ProductListSearch.route, arguments = listOf(
                navArgument("param") {},
            )
        ) {
            val query = it.arguments?.getString("param")
            ProductList(
                navController,
                title = "Kết quả tìm kiếm cho $query",
                param = "$query",
                type = "",
                cid = "", //-1
                filterStatus = 0
            )
        }

        composable(
            route = Screen.Product.route, arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            val id = it.arguments?.getString("id")

            if (id != null) {
                ProductDetails(navController = navController, id)
            }
        }
        composable(
            route = "${Screen.ProductListFilter.route}/{id}/{type}", arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            ProductListFilterInit(
                navController,
                backStackEntry.arguments?.getString("id"),
                backStackEntry.arguments?.getString("type"),
            )
        }

        composable(
            route = Screen.AdminCategoryList.route,
        ) {
            CategoryList(navController, "Quản lý danh mục", "Admin", "")
        }

        composable(
            route = Screen.ManageCategory.route, arguments = listOf(
                navArgument("actionType") { type = NavType.StringType },
            )
        ) {
            val actionType = it.arguments?.getString("actionType")
            ManageCategoryInit(navController = navController, actionType = actionType)
        }
        composable(
            route = "${Screen.CategoryList.route}/{title}/{type}/{cid}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType },
                navArgument("cid") { type = NavType.StringType },
            )
        ) {
            val title = it.arguments?.getString("title")
            val type = it.arguments?.getString("type")
            val cid = it.arguments?.getString("cid")
            CategoryList(navController, "$title", "$type", "")
        }

        composable(
            route = Screen.AdminCouponList.route,
        ) {
            CouponList(navController, "Quản lý mã giảm giá", "Admin")
        }

        composable(
            route = Screen.ManageCoupon.route, arguments = listOf(
                navArgument("actionType") { type = NavType.StringType },
            )
        ) {
            val actionType = it.arguments?.getString("actionType")
            ManageCouponInit(navController = navController, actionType = actionType)
        }
        composable(
            route = "${Screen.CouponList.route}/{title}/{type}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType },
            )
        ) {
            val title = it.arguments?.getString("title")
            val type = it.arguments?.getString("type")
            CouponList(navController, "$title", "$type")
        }

    }
}

