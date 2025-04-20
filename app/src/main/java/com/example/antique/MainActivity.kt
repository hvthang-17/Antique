package com.example.antique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.antique.view.theme.AntiqueTheme
import com.example.antique.view.navigation.NavigationGraph
import com.example.antique.viewmodel.AppViewModel

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiqueTheme {
                val context = LocalContext.current
                val appViewModel = viewModel<AppViewModel>(
                    viewModelStoreOwner = context as ComponentActivity
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationGraph(navController = rememberNavController())
                }
            }
        }
    }
}
