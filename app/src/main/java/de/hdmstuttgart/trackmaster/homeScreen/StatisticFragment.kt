package de.hdmstuttgart.trackmaster.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.TrackMasterApplication
import de.hdmstuttgart.trackmaster.data.BarchartInput
import de.hdmstuttgart.trackmaster.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StatisticFragment : Fragment(R.layout.fragment_statistic) {

    private val defaultMaxHeight = 100.dp


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_statistic, container, false).apply {
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed

            findViewById<ComposeView>(R.id.composeView).setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {
                    BarChart(getInput())
                }
            }
        }
    }

   private fun getInput(): List<BarchartInput> {
        val barchartInputList: MutableList<BarchartInput> = mutableListOf()

        activity?.let {
            val fragmentActivity = it
            val trackMasterApplication = fragmentActivity.application as TrackMasterApplication

            lifecycleScope.launch(Dispatchers.IO) {


                //todo: remove test tracks
                trackMasterApplication.repository.deleteAll()
                trackMasterApplication.repository.insert(Track(distance = 2, time = 20))
                trackMasterApplication.repository.insert(Track(distance = 5, time = 45))

                val allTracks = trackMasterApplication.repository.getAllTracks()
                for(track in allTracks) {
                    val input = BarchartInput(track.distance, track.date.toString())
                    barchartInputList.add(input)
                }
            }
        }
       return barchartInputList
    }


    @Composable
    fun BarChart(
        inputList: List<BarchartInput>,
        modifier: Modifier = Modifier,
        maxHeight: Dp = defaultMaxHeight
    ) {
        if(inputList.isNotEmpty()) {
            Text(text =  "No data, start tracking your activities.")
        }

        val borderColor = Color(R.color.black)
        val density = LocalDensity.current
        val strokeWidth = with(density) { 1.dp.toPx() }

        Row(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(maxHeight)
                    .drawBehind {
                        // draw X-Axis
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                        // draw Y-Axis
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            inputList.forEach { item ->
                Bar(
                    input = item,
                    color = Color(0xFF03DAC5), //todo: change color (add it to colors.xml and reference it here)
                    maxHeight = maxHeight,
                    description = item.date
                )
            }
        }
    }

    @Composable
    fun RowScope.Bar(
        input: BarchartInput,
        color: Color,
        maxHeight: Dp,
        description: String
    ) {
        val value = input.distance
        val itemHeight = remember(value) { value * maxHeight.value / 100 }

        Spacer(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(itemHeight.dp)
                .weight(1f)
                .background(color)
        )
    }




    //old version

    /*Row(
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
        }*/

    /*@Composable
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
            }
        }
    }*/
}