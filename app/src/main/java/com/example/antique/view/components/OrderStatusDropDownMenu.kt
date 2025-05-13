package com.example.antique.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.antique.model.remote.entity.OrderStatus
import com.example.antique.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusDropDownMenu(
    orderViewModel: OrderViewModel,
    options: List<OrderStatus>
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = orderViewModel.orderStatus.value

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption.code,
            onValueChange = {},
            readOnly = true,
            label = { Text("Chọn trạng thái") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.code) },
                    onClick = {
                        orderViewModel.orderStatus.value = status
                        orderViewModel.isOrderUpdated.value = true
                        expanded = false
                    }
                )
            }
        }
    }
}
