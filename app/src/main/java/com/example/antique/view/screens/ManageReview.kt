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

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }, topBar = {

        var type = ""
        reviewViewModel.returnReview()?.let { type = "Edit" } ?: run { type = "Add" }
        TopBar(type, { navController.popBackStack() }, actions = {
            if (type == "Edit") {
                IconButton(onClick = {
                    reviewViewModel.openDialog = true
                }) {
                    Row(Modifier.fillMaxWidth(0.5f)) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Review",
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
                        contentDescription = "placeholder image",
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
                            fontSize = 20.sp
                        )
                        RatingBar(reviewViewModel)
                    }
                }
            }

            Column() {
                Text(text = "Review Title", fontSize = 14.sp)
                OutlinedTextField(
                    modifier = inputFieldModifier,
                    value = reviewViewModel.reviewTitle,
                    onValueChange = {
                        if (reviewViewModel.titleError) reviewViewModel.titleError = false
                        reviewViewModel.reviewTitle = it
                    },
                    placeholder = { Text("Heading") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = reviewViewModel.titleError
                )
            }

            Column() {
                Text(text = "Description", fontSize = 14.sp)
                OutlinedTextField(
                    modifier = inputFieldModifier.fillMaxHeight(0.5f),
                    value = reviewViewModel.reviewDescription,
                    onValueChange = {
                        if (reviewViewModel.descriptionError) reviewViewModel.descriptionError =
                            false
                        reviewViewModel.reviewDescription = it
                    },
                    placeholder = { Text("Details") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = reviewViewModel.descriptionError
                )
            }

            Button(modifier = Modifier
                .align(Alignment.End)
                .padding(top = 50.dp), onClick = {}) {
                Text(text = "Submit", fontSize = 20.sp, modifier = Modifier.clickable {
                    val allowSubmit: Boolean = reviewViewModel.validateReviewInput()
                    reviewViewModel.returnReview()?.let {
                        if (allowSubmit) {
                            reviewViewModel.updateReview()
                            Toast.makeText(context, "Review Updated", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    } ?: run {

                        if (allowSubmit) {
                            reviewViewModel.addReview()
                            Toast.makeText(context, "Review Added", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                })
            }

            if (reviewViewModel.openDialog) {
                AlertDialog(onDismissRequest = { reviewViewModel.openDialog = false },
                    title = { Text("Are you sure you want to delete?") },
                    text = {
                        Column() {
                            Text(
                                "This will delete your review of the product.",
                                Modifier.padding(top = 10.dp)
                            )
                        }
                    },
                    confirmButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            reviewViewModel.openDialog = false
                            reviewViewModel.deleteReview()
                            Toast.makeText(context, "Review Deleted", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            reviewViewModel.openDialog = false

                        }) {
                            Text("Cancel")
                        }
                    })
            }
        }
    }, bottomBar = { UserBottomBar(navController) })
}

@Composable
fun RatingBar(reviewViewModel: ReviewViewModel) {
    Column() {
        Text(text = "Select rating: ", fontSize = 12.sp)
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


