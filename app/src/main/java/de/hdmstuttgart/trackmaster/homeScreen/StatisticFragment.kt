package de.hdmstuttgart.trackmaster.homeScreen

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.data.BarchartInput


class StatisticFragment : Fragment(R.layout.fragment_statistic) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // some test tracks //todo: remove later

    }

    /*@Composable
    fun BarChart(
        inputList: List<BarchartInput>,
        modifier: Modifier = Modifier,
        showDescription: Boolean
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            val listSum by remember {
                mutableStateOf(inputList.sumOf { it.value })
            }
            inputList.forEach { input ->
                val percentage = input.value / listSum.toFloat()
                Bar(
                    modifier = Modifier
                        .height(120.dp * percentage * inputList.size)
                        .width(40.dp),
                    percentage = input.value / listSum.toFloat(),
                    description = input.timespan,
                    showDescription = showDescription
                )
            }
        }
    }

    @Composable
    fun Bar(
        modifier: Modifier = Modifier,
        primaryColor: Color = Color(0xFF03DAC5), //todo: change color (add it to colors.xml and reference it here)
        percentage: Float,
        description: String,
        showDescription: Boolean
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    moveTo(0f, height)
                    lineTo(width, height)
                    lineTo(width, 0f)
                    lineTo(0f, 0f)
                    close()
                }
                drawPath(
                    path,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(("0x" + R.color.black).toInt()), primaryColor)
                    )
                )

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "date", //todo: here has to stand the date/ week/ month
                        width / 5f,
                        height + 55f,
                        android.graphics.Paint().apply {
                            this.color = R.color.black.toColor().toArgb()
                            textSize = 11.dp.toPx()
                            isFakeBoldText = true
                        }
                    )
                }
                if (showDescription) {
                    //todo: replace by horizontal lines for tracked kilometres
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            description,
                            0f,
                            0f,
                            android.graphics.Paint().apply {
                                this.color = R.color.white.toColor().toArgb()
                                textSize = 14.dp.toPx()
                                isFakeBoldText = true
                            }
                        )
                    }
                }
            }
        }
    }*/
}