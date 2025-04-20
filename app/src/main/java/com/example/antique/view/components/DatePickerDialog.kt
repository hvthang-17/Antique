package com.example.antique.view.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.*
import java.util.Calendar

fun DatePickerDialog(
    mutableDate: MutableState<String>,
    context: Context,
    calendar: Calendar,
    maxDate: Long?
): DatePickerDialog {
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.add(Calendar.YEAR, -18)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            mutableDate.value = "${if(month< 9) {"0" +  month.plus(1)} else {month + 1}}/${if(dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}/$year"
        }, year, day, month
    )
    maxDate?.let { datePickerDialog.datePicker.maxDate = maxDate }

    return datePickerDialog
}