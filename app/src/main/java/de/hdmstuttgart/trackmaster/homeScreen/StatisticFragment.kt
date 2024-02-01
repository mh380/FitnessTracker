package de.hdmstuttgart.trackmaster.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.TrackMasterApplication
import de.hdmstuttgart.trackmaster.data.BarchartInput
import de.hdmstuttgart.trackmaster.data.Track
import de.hdmstuttgart.trackmaster.data.toDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale


class StatisticFragment : Fragment(R.layout.fragment_statistic) {

    private val defaultMaxHeight = 200.dp //defines max height for bars
    private var barchartInputList: MutableList<BarchartInput> = mutableListOf()
    private var description = "this week"
    private var gap = 5

    private var listMax = 0
    private var currentTimeSpan = "Week"

    private lateinit var inflater: LayoutInflater
    private var container: ViewGroup? = null
    private var savedInstanceState: Bundle? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.inflater = inflater
        this.container = container
        this.savedInstanceState = savedInstanceState
        return inflater.inflate(R.layout.fragment_statistic, container, false).apply {
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed

            update(container)
        }
    }

    private fun update(container: ViewGroup?) {

            lifecycleScope.launch(Dispatchers.IO) {
                getInput()

                withContext(Dispatchers.Main) {
                    container!!.findViewById<ComposeView>(R.id.composeView).setContent {
                        Surface(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                                .testTag("surface"),
                            color = MaterialTheme.colors.viewBackgroundColor,
                            shape = RoundedCornerShape(16.dp),
                            elevation = 8.dp,
                        ) {
                            if (barchartInputList.isEmpty()) {
                                Text(text = "No data, start tracking your activities.")
                            } else {
                                BarChart(barchartInputList)
                            }
                        }
                    }
                }
            }
        }

    private fun getInput() {

        val currentDate = LocalDate.now()

        activity?.let {
            val fragmentActivity = it
            val trackMasterApplication = fragmentActivity.application as TrackMasterApplication

            lifecycleScope.launch(Dispatchers.IO) {

                barchartInputList.clear()
                val allTracks: List<Track>

                when (currentTimeSpan) {
                    "Week" -> {
                        // Get the start and end date of the current week
                        val startDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                        val endDate = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

                        allTracks = trackMasterApplication.repository.getTracksFromWeek(startDate, endDate)
                        getWeekInput(startDate, allTracks)

                        description = "this week"
                        gap = 5
                    }
                    "Month" -> {
                        allTracks = trackMasterApplication.repository.getTracksFromMonth(currentDate.month)
                        getMonthInput(currentDate, allTracks)

                        description = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                        gap = 1
                    }
                    "Year" -> {
                        allTracks = trackMasterApplication.repository.getTracksFromYear(currentDate.year.toString())
                        getYearInput(allTracks)

                        description = currentDate.year.toString()
                        gap = 3
                    }
                }
            }
        }
    }


    @Composable
    fun BarChart(
        inputList: List<BarchartInput>
    ) {

        val borderColor = MaterialTheme.colors.viewTextColor
        val density = LocalDensity.current
        val strokeWidth = with(density) { 1.dp.toPx() }

        Column(
            modifier = Modifier.padding(8.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Your Statistics for $description",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.viewTextColor,
                    modifier = Modifier
                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                        .testTag("statisticsText"),
                )
                DropDown()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(defaultMaxHeight)
                    .padding(0.dp, 16.dp, 0.dp, 0.dp)
                    .drawBehind {
                        // draw X-Axis
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                listMax = inputList.maxWith(Comparator.comparingInt { it.distance }).distance
                inputList.forEach { item ->
                    Bar(
                        input = item
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                inputList.forEach { item ->
                    Text(
                        color = MaterialTheme.colors.viewTextColor,
                        text = item.dateString
                    )
                }
            }
        }
    }

    @Composable
    fun RowScope.Bar(
        input: BarchartInput
    ) {
        val color = colorResource(R.color.sky_blue)
        val value = input.distance
        val itemHeight = remember(value) { value * defaultMaxHeight.value / listMax }

        Spacer(
            modifier = Modifier
                .padding(horizontal = gap.dp)
                .height(itemHeight.dp)
                .weight(1f)
                .background(color)
        )
    }


    @Composable
    fun DropDown() {
        val list = listOf("Week", "Month", "Year")
        val expanded = remember { mutableStateOf(false) }
        val currentValue = remember { mutableStateOf(list[0]) }

        Box {
            Row(
                modifier = Modifier
                    .clickable { expanded.value = !expanded.value }
                    .align(Alignment.TopEnd)
                    .testTag("dropdown")
            ) {
                Text(text = currentValue.value,
                    color = MaterialTheme.colors.viewTextColor)
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null,
                    tint = MaterialTheme.colors.viewTextColor)

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                ) {
                    list.forEach {
                        DropdownMenuItem(
                            onClick = {
                                currentValue.value = it
                                currentTimeSpan = it
                                expanded.value = false
                                update(container)
                            }
                        ) {
                            Text(text = it)
                        }
                    }
                }
            }
        }
    }

    private fun getWeekInput(startDate: LocalDate, allTracks: List<Track>) {
        for(i in 1..7) {
            val tracksOfDay = allTracks.filter { track -> track.date == startDate.plusDays(i-1L) }
            val sum = sumTracks(tracksOfDay)
            val input = BarchartInput(sum, DayOfWeek.of(i).getDisplayName(TextStyle.SHORT, Locale.getDefault()))    //toDayAndMonth(startDate.plusDays(i.toLong()))
            barchartInputList.add(input)
        }
    }

    private fun getMonthInput(currentDate: LocalDate, allTracks: List<Track>) {
        val length = currentDate.month.length(currentDate.isLeapYear)
        val startDate = currentDate.withDayOfMonth(1)
        for(i in 1..length) {
            val tracksOfDay = allTracks.filter { track -> track.date == startDate.plusDays(i-1L) }
            val sum = sumTracks(tracksOfDay)
            val input = BarchartInput(sum, toDay(startDate.plusDays(i-1L))) //todo: change that bc it looks awful
            barchartInputList.add(input)
        }
    }

    private fun getYearInput(allTracks: List<Track>) {
        for(i in 1..12) {
            val tracksOfMonth = allTracks.filter { track -> track.date.month == Month.of(i)}
            val sum = sumTracks(tracksOfMonth)
            val input = BarchartInput(sum, Month.of(i).getDisplayName(TextStyle.SHORT, Locale.getDefault()))
            barchartInputList.add(input)
        }
    }

    private fun sumTracks(tracksOfDay: List<Track>): Int {
        var sum = 0
        for(trackOfDay in tracksOfDay) {
            sum += trackOfDay.distance
        }
        return sum
    }


    // Color composable contains colors that change with the light/dark theme.
   private val Colors.viewBackgroundColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) colorResource(R.color.L_background_white) else colorResource(R.color.D_secondary_blue)
    private val Colors.viewTextColor: Color
        @Composable
        get() = if (!isSystemInDarkTheme()) colorResource(R.color.L_text_black) else colorResource(R.color.D_text_white)

}