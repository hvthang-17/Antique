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
fun PriceFilterBottomSheet(filterVM: FilterViewModel = viewModel(LocalContext.current as ComponentActivity)) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    fun validatePriceInput() {
        if (filterVM.startPriceText.isBlank() || filterVM.endPriceText.isBlank()) {
            Toast.makeText(context, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show()
        } else {
            if (filterVM.startPriceText.toFloat() >= filterVM.endPriceText.toFloat()) {
                Toast.makeText(
                    context, "Giá tối thiểu phải thấp hơn giá tối đa", Toast.LENGTH_SHORT
                ).show()
            } else {
                filterVM.setPriceRange(
                    filterVM.startPriceText.toInt().toFloat()..filterVM.endPriceText.toInt()
                        .toFloat()
                )
                filterVM.setPriceEnd(
                    filterVM.startPriceText.toInt().toFloat()..filterVM.endPriceText.toInt()
                        .toFloat()
                )
            }
        }
    }


    Column(
        modifier = Modifier
            .selectableGroup()
            .padding(10.dp)
    ) {

        Divider(
            modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nhập giá",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            softWrap = true,
            overflow = TextOverflow.Clip,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.size(300.dp)
        ) {
            TextField(
                modifier = Modifier.size(120.dp, 100.dp),
                value = filterVM.startPriceText,
                onValueChange = { filterVM.startPriceText = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(
                        FocusDirection.Right
                    )
                }),
                label = { Text(text = "Giá tối thiểu") },
                textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
                placeholder = { Text(filterVM.startPriceRange.toString()) },

                )
            TextField(
                modifier = Modifier.size(120.dp, 100.dp),
                value = filterVM.endPriceText,
                onValueChange = { filterVM.endPriceText = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    validatePriceInput()
                    // handleLogin()
                }),
                label = { Text(text = "Giá tối đa") },
                placeholder = { Text(filterVM.endPriceRange.toString()) },
                textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
            )
        }
    }

}