package com.example.antique.view.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.antique.view.components.TopBar
import com.example.antique.view.components.UserBottomBar
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.ReviewViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ContextCastToActivity")
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageReview(
    navController: NavHostController,
    productId: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    reviewViewModel: ReviewViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    reviewViewModel.setUserId(appViewModel.getCurrentUserId())
    reviewViewModel.setProductId(productId)

    val product = reviewViewModel.getProduct()

    val inputFieldModifier = Modifier
        .padding(top = 5.dp)
        .fillMaxWidth()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFF8EBCB),
        modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }, topBar = {

        var type = ""
        reviewViewModel.returnReview()?.let { type = "Sửa" } ?: run { type = "Thêm" }
        TopBar(type, { navController.popBackStack() }, actions = {
            if (type == "Sửa") {
                IconButton(onClick = {
                    reviewViewModel.openDialog = true
                }) {
                    Row(Modifier.fillMaxWidth(0.5f)) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Xóa đánh giá",
                            tint = Color.Red
                        )
                    }
                }
            }
        })
    }, content = { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
            product?.let {
                Row(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
                    Image(
                        painter = rememberAsyncImagePainter(model = it.image),
                        contentDescription = "Ảnh sản phẩm",
                        Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.width(32.dp))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = it.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 20.sp,
                            color = Color(0xFF4B1E1E)
                        )
                        RatingBar(reviewViewModel)
                    }
                }
            }

            Column() {
                Text(text = "Tiêu đề", fontSize = 14.sp, color = Color(0xFF6D4C41))
                OutlinedTextField(
                    modifier = inputFieldModifier,
                    value = reviewViewModel.reviewTitle,
                    onValueChange = {
                        if (reviewViewModel.titleError) reviewViewModel.titleError = false
                        reviewViewModel.reviewTitle = it
                    },
                    placeholder = { Text("Nhập tiêu đề", color = Color(0xFF6D4C41)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = reviewViewModel.titleError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6D4C41),
                        unfocusedBorderColor = Color(0xFF6D4C41),
                        cursorColor = Color(0xFF6D4C41)
                    )
                )
            }

            Column() {
                Text(text = "Nội dung", fontSize = 14.sp, color = Color(0xFF6D4C41))
                OutlinedTextField(
                    modifier = inputFieldModifier.fillMaxHeight(0.5f),
                    value = reviewViewModel.reviewDescription,
                    onValueChange = {
                        if (reviewViewModel.descriptionError) reviewViewModel.descriptionError =
                            false
                        reviewViewModel.reviewDescription = it
                    },
                    placeholder = { Text("Chi tiết đánh giá", color = Color(0xFF6D4C41)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = reviewViewModel.descriptionError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6D4C41),
                        unfocusedBorderColor = Color(0xFF6D4C41),
                        cursorColor = Color(0xFF6D4C41)
                    )
                )
            }

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp),
                onClick = {
                    val allowSubmit = reviewViewModel.validateReviewInput()
                    if (reviewViewModel.returnReview() != null) {
                        if (allowSubmit) {
                            reviewViewModel.updateReview()
                            Toast.makeText(context, "Đã cập nhật đánh giá", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    } else {
                        if (allowSubmit) {
                            reviewViewModel.addReview()
                            Toast.makeText(context, "Đã thêm đánh giá", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D4C41),
                    contentColor = Color(0xFFF8EBCB)
                )
            ) {
                Text(text = "Gửi", fontSize = 18.sp)
            }

            if (reviewViewModel.openDialog) {
                AlertDialog(onDismissRequest = { reviewViewModel.openDialog = false },
                    title = { Text("Xác nhận xóa đánh giá") },
                    text = {
                        Column() {
                            Text(
                                "Bạn có chắc chắn muốn xóa đánh giá sản phẩm này?",
                                Modifier.padding(top = 10.dp)
                            )
                        }
                    },
                    confirmButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            reviewViewModel.openDialog = false
                            reviewViewModel.deleteReview()
                            Toast.makeText(context, "Đã xóa đánh giá", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }) {
                            Text("Xác nhận")
                        }
                    },
                    dismissButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            reviewViewModel.openDialog = false

                        }) {
                            Text("Hủy")
                        }
                    })
            }
        }
    }, bottomBar = { UserBottomBar(navController) })
}

@Composable
fun RatingBar(reviewViewModel: ReviewViewModel) {
    Column() {
        Text(text = "Chọn số sao:", fontSize = 12.sp, color = Color(0xFF6D4C41))
        Row() {
            for (i in 1..5) {
                Icon(
                    imageVector = Icons.Filled.StarRate,
                    contentDescription = "star",
                    modifier = Modifier
                        .requiredWidthIn(28.dp, 35.dp)
                        .height(32.dp)
                        .clickable {
                            reviewViewModel.ratingValue = i
                        },
                    tint = if (i <= reviewViewModel.ratingValue) Color(255, 215, 0) else Color.Gray
                )
            }
        }
    }
}


