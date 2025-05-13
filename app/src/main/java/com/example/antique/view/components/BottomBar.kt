package com.example.antique.view.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.antique.view.navigation.Screen

@ExperimentalMaterial3Api
@Composable
fun UserBottomBar(navController: NavHostController) {
    val screens = listOf(Screen.Home, Screen.Cart, Screen.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(Modifier.height(55.dp),
        containerColor = Color(0xFF8C6A3F),
        contentColor = Color(0xFFF8EBCB)  ){
        screens.forEach { screen ->
            if (currentDestination != null) {
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun AdminBottomBar(navController: NavHostController) {
    val screens = listOf(Screen.Home, Screen.Cart, Screen.Dashboard, Screen.AdminReport,  Screen.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(Modifier
        .height(55.dp),
        containerColor = Color(0xFF8C6A3F),
        contentColor = Color(0xFFF8EBCB)) {
        screens.forEach { screen ->
            if (currentDestination != null) {
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen, currentDestination: NavDestination, navController: NavHostController
) {
    NavigationBarItem(selected = currentDestination.hierarchy.any {
        it.route == screen.route
    }, onClick = {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }, icon = {
        Icon(
            imageVector = screen.icon,
            contentDescription = "Icon điều hướng"
        )
    },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFFF8EBCB),
            unselectedIconColor = Color(0xFFD7BA9C),
            indicatorColor = Color(0xFF8B5E3C)
        )
    )
}
