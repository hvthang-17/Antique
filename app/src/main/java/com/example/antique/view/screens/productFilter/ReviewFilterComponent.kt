package com.example.antique.view.screens.productFilter

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antique.viewmodel.FilterViewModel
import kotlin.math.roundToInt

@SuppressLint("ContextCastToActivity")
@Composable
fun ReviewFilterComponent(
    filterVM: FilterViewModel = viewModel(
        LocalContext.current as ComponentActivity
    )
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Đánh giá tối thiểu",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            softWrap = true,
            color = Color(0xFF4B1E1E),
            overflow = TextOverflow.Clip,
        )
        TextButton(
            onClick = {
                filterVM.setReviewSliderValue(5f)
                filterVM.sliderEnd = 5f
            },
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4B1E1E))
        ) {
            Text("Đặt lại")
        }
    }

    ReviewSlider(filterVM.steps, filterVM.reviewRange)
}

@SuppressLint("ContextCastToActivity")
@Composable
fun ReviewSlider(
    steps: Int = 0,
    valueRange: ClosedFloatingPointRange<Float>,
    filterViewModel: FilterViewModel = viewModel(
        LocalContext.current as ComponentActivity
    )
) {
    Text(
        text = "${filterViewModel.getReviewSliderValue().roundToInt()} sao trở lên",
        fontSize = 16.sp,
        color = Color(0xFF4B1E1E),
        fontWeight = FontWeight.Medium
    )

    Slider(modifier = Modifier.semantics { contentDescription = "Minimum stars slider" },
        value = filterViewModel.getReviewSliderValue(),
        onValueChange = { filterViewModel.setReviewSliderValue(it) },
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = {
            filterViewModel.sliderEnd = filterViewModel.reviewSlider
        },
        colors = SliderDefaults.colors(
            thumbColor = Color(0xFF6D4C41),
            activeTrackColor = Color(0xFF4B1E1E),
            inactiveTrackColor = Color(0xFFBCAAA4)
        )
    )
}