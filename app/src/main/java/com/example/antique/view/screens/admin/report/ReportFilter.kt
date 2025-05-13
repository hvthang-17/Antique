package com.example.antique.view.screens.admin.report

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.antique.model.remote.entity.Order
import com.example.antique.view.components.DatePickerDialog
import com.example.antique.view.components.TopBar
import com.example.antique.viewmodel.admin.ReportVM
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportFilter(
    navController: NavHostController,
    @SuppressLint("ContextCastToActivity") reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
) {
    val currentDate: LocalDate = LocalDate.now()
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val currentDateFormatted: String = currentDate.format(formatter)

    var error by remember { mutableStateOf(false) }
    val startDate = remember { mutableStateOf("N/A") }
    val endDate = remember { mutableStateOf(currentDateFormatted) }

    Scaffold(
        containerColor = Color(0xFFEAD9B5),
        topBar = {
        TopBar("Lọc báo cáo", { navController.popBackStack() }, actions = {
            TextButton(onClick = {

                if (startDate.value != "N/A" && LocalDate.parse(endDate.value, formatter)
                        .isBefore(LocalDate.parse(startDate.value, formatter))
                ) {
                    error = true
                } else {
                    reportVM.startDate.value = startDate.value
                    reportVM.endDate.value = endDate.value
                    reportVM.finalOrderStatusValue.value = reportVM.tempOrderStatusValue.value
                    reportVM.filterReport("report")?.toMutableList()
                        ?: emptyList<Order>().toMutableList()
                    reportVM.isFiltered = true

                    navController.popBackStack()
                }
            }) {
                Text(text = "Áp dụng", color = Color.White)
            }
        })
    }, content = { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(12.dp)
        ) {
            val context = LocalContext.current
            if (error) {
                Toast.makeText(context, "Ngày kết thúc không được sau ngày bắt đầu!", Toast.LENGTH_SHORT)
                    .show()
                error = false
            }
            val focusManager = LocalFocusManager.current

            Text(text = "Trạng thái", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B1E1E))
            StatusRadioButton()
            Text(text = "Ngày bắt đầu", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B1E1E))
            //Date Picker
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -1)

            val dateDialog = DatePickerDialog(
                startDate, context, calendar, calendar.timeInMillis
            )
            OutlinedTextField(
                value = startDate.value,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                    dateDialog.show()
                },
                onValueChange = {},
                singleLine = true,
                enabled = false,
                label = { Text("Ngày bắt đầu", color = Color(0xFF4B1E1E)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6D4C41),
                    unfocusedBorderColor = Color(0xFF6D4C41),
                    disabledTextColor = Color(0xFF4B1E1E),
                    disabledLabelColor = Color(0xFF4B1E1E),
                    disabledBorderColor = Color(0xFF6D4C41),
                    disabledPlaceholderColor = Color(0xFF4B1E1E)
                )
            )

            Text(text = "Ngày kết thúc", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B1E1E))
            //Date Picker
            val calendar2 = Calendar.getInstance()
            val dateDialog2 = DatePickerDialog(
                endDate , context, calendar2, calendar2.timeInMillis
            )
            OutlinedTextField(
                value = endDate.value,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                    dateDialog2.show()
                },
                onValueChange = {},
                singleLine = true,
                enabled = false,
                label = { Text("Ngày kết thúc", color = Color(0xFF4B1E1E)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6D4C41),
                    unfocusedBorderColor = Color(0xFF6D4C41),
                    disabledTextColor = Color(0xFF4B1E1E),
                    disabledLabelColor = Color(0xFF4B1E1E),
                    disabledBorderColor = Color(0xFF6D4C41),
                    disabledPlaceholderColor = Color(0xFF4B1E1E)
                )
            )
        }
    })
}

@SuppressLint("ContextCastToActivity")
@Composable
fun StatusRadioButton() {
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)
// Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        reportVM.radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text ==  reportVM.tempOrderStatusValue.value),
                        onClick = {  reportVM.tempOrderStatusValue.value = text },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text ==  reportVM.tempOrderStatusValue.value),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = text,
                    color = Color(0xFF4B1E1E),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
