package com.example.antique.view.screens.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.SearchBarState
import com.example.antique.view.components.TopBar
import com.example.antique.viewmodel.HomeViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun HomeTopBar(homeViewModel: HomeViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val searchBarState by homeViewModel.searchBarState
    val searchTextState by homeViewModel.searchTextState
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)

    when (searchBarState) {
        SearchBarState.CLOSED -> {
            TopBar(title = "Trang chủ", actions = {
                IconButton(onClick = { homeViewModel.updateSearchBarState(newValue = SearchBarState.OPENED) }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Black
                    )
                }
            })
        }
        SearchBarState.OPENED -> {
            SearchHomeTopBar(text = searchTextState,
                onTextChange = { homeViewModel.updateSearchTextState(newValue = it) },
                onCloseClicked = { homeViewModel.updateSearchBarState(newValue = SearchBarState.CLOSED) },
                onSearchClicked = {
                    if (searchTextState == "") {
                        Toast.makeText(context, "Truy vấn tìm kiếm không hợp lệ", Toast.LENGTH_SHORT).show()
                    } else {
                        homeVM.actionType = "search"
                        navController.navigate("ProductListSearch/$searchTextState")
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHomeTopBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(0.dp)),
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        val focusRequester = remember { FocusRequester() }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.7f), text = "Tìm kiếm...", color = Color.Black
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(modifier = Modifier.alpha(0.7f), onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Black
                    )
                }
            },
            trailingIcon = {
                IconButton(modifier = Modifier.alpha(0.7f), onClick = {
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.Black
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClicked(text)
            }),
//            colors = TextFieldDefaults.textFieldColors(
//                cursorColor = Color.Black.copy(alpha = 0.7f), containerColor = Color.Transparent
//            )
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}