package com.example.antique.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalMaterial3Api
@Composable
fun SuccessDialog(
    openDialog: MutableState<Boolean>,
    title: String = "Thành công",
    description: String = "Hành động thành công",
    confirmBtnText: String = "Đóng",
    confirmNavFn: () -> Unit = {}
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                confirmNavFn()
            },
            containerColor = Color(0xFFF8EBCB),
            titleContentColor = Color(0xFF5D3A00),
            textContentColor = Color(0xFF3F2C1B),
            title = {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D3A00)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3F2C1B)
                    )
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            openDialog.value = false
                            confirmNavFn()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8C6A3F),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = confirmBtnText, fontSize = 16.sp)
                    }
                }
            }
        )
    }
}
