package com.example.antique.view.screens.productFilter

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antique.viewmodel.FilterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun PriceFilterBottomSheet(
    filterVM: FilterViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    fun validatePriceInput() {
        if (filterVM.startPriceText.isBlank() || filterVM.endPriceText.isBlank()) {
            Toast.makeText(context, "Vui lòng nhập đầy đủ giá", Toast.LENGTH_SHORT).show()
        } else {
            if (filterVM.startPriceText.toFloat() >= filterVM.endPriceText.toFloat()) {
                Toast.makeText(
                    context, "Giá tối thiểu phải nhỏ hơn giá tối đa", Toast.LENGTH_SHORT
                ).show()
            } else {
                val range = filterVM.startPriceText.toFloat()..filterVM.endPriceText.toFloat()
                filterVM.setPriceRange(range)
                filterVM.setPriceEnd(range)
            }
        }
    }

    Column(
        modifier = Modifier
            .selectableGroup()
            .padding(16.dp)
    ) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF6D4C41)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Nhập khoảng giá",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color(0xFF4B1E1E)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .width(150.dp)
                    .height(80.dp),
                value = filterVM.startPriceText,
                onValueChange = { filterVM.startPriceText = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.moveFocus(FocusDirection.Right) }
                ),
                label = {
                    Text("Giá từ", color = Color(0xFF6D4C41))
                },
                placeholder = {
                    Text(filterVM.startPriceRange.toString(), color = Color(0xFFBCAAA4))
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF4B1E1E)
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            TextField(
                modifier = Modifier
                    .width(150.dp)
                    .height(80.dp),
                value = filterVM.endPriceText,
                onValueChange = { filterVM.endPriceText = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        validatePriceInput()
                    }
                ),
                label = {
                    Text("Đến", color = Color(0xFF6D4C41))
                },
                placeholder = {
                    Text(filterVM.endPriceRange.toString(), color = Color(0xFFBCAAA4))
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF4B1E1E)
                )
            )
        }
    }
}
