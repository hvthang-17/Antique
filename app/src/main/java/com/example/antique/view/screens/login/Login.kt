package com.example.antique.view.screens.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antique.R
import com.example.antique.view.components.toAdaptiveDp
import com.example.antique.viewmodel.AppViewModel
import com.example.antique.viewmodel.LoginViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Login(
    onSuccessfulLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotClick: () -> Unit,
    @SuppressLint("ContextCastToActivity") appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    @SuppressLint("ContextCastToActivity") viewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(20.toAdaptiveDp())
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        fun handleLogin() {
            if (viewModel.validateLoginInput()) {
                if (viewModel.authenticateLogin()) {
                    appViewModel.setCurrentUserId(viewModel.getCurrentUserId())
                    viewModel.password = ""
                    onSuccessfulLogin()
                } else {
                    Toast.makeText(context, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Missing login details!", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(5.toAdaptiveDp()))
        Image(
            painter = painterResource(id = R.drawable.ruong),
            contentDescription = "App logo",
            Modifier
                .height(180.toAdaptiveDp())
                .padding(5.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        OutlinedTextField(value = viewModel.email,
            onValueChange = {
                if (viewModel.emailError) viewModel.emailError = false
                viewModel.email = it
            },
            Modifier
                .padding(top = 20.toAdaptiveDp())
                .fillMaxWidth(),
            label = { Text(text = "Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
            isError = viewModel.emailError
        )

        val inputFieldModifier = Modifier
            .padding(top = 10.toAdaptiveDp())
            .fillMaxWidth()

        OutlinedTextField(value = viewModel.password,
            modifier = inputFieldModifier,
            onValueChange = {
                if (viewModel.passwordError) viewModel.passwordError = false
                viewModel.password = it
            },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                handleLogin()
            }),
            isError = viewModel.passwordError,
            trailingIcon = {
                val image =
                    if (viewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description =
                    if (viewModel.passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { viewModel.passwordVisible = !viewModel.passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            })

        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Text("Forgot Password?",
                Modifier
                    .padding(top = 10.toAdaptiveDp())
                    .clickable() { onForgotClick() })
        }
        Button(
            onClick = { handleLogin() }, Modifier.padding(top = 20.toAdaptiveDp())
        ) {
            Text(
                text = "Log In",
                Modifier.padding(start = 20.toAdaptiveDp(), end = 20.toAdaptiveDp()),
                fontSize = 18.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.height(5.toAdaptiveDp()))


            Text(
                text = "New user? Register Here",
                Modifier
                    .padding(top = 10.toAdaptiveDp(), bottom = 15.toAdaptiveDp())
                    .clickable() { onRegisterClick() },
                fontSize = 16.sp
            )

//            Text("OR SIGN UP USING")
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(top = 10.toAdaptiveDp()),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.google),
//                    contentDescription = "Google",
//                    Modifier.size(50.toAdaptiveDp())
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.facebook),
//                    contentDescription = "FaceBook",
//                    Modifier.size(50.toAdaptiveDp())
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.twitter),
//                    contentDescription = "Twitter",
//                    Modifier.size(50.toAdaptiveDp())
//                )
//            }
        }
    }
}