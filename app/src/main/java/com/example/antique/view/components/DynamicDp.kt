package com.example.antique.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Int.toAdaptiveDp(): Dp { //dpValue: Int
    val configuration = LocalConfiguration.current
    val currentDensityDpi = configuration.densityDpi
    //   val pixels = this * (densityDpi/160.0)
    val minDensity = 160.0
    //if nothing else works
    // 640/160
    // dpValue/ above , e.g 140/ 4

    /* val getUpScaleDpRatio = 640/560.0
     val upscaleDpTo640 = getUpScaleDpRatio * this*/
    val totalDpi = (this * minDensity)
    val dpValueOf160 = totalDpi / 560.0
    val dpDiff = currentDensityDpi / 160.0
    val adjustedDp = dpValueOf160 * dpDiff

    return adjustedDp.roundToInt().dp

}