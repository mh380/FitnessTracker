package de.hdmstuttgart.trackmaster

import de.hdmstuttgart.trackmaster.data.Track
import de.hdmstuttgart.trackmaster.homeScreen.StatisticFragment
import de.hdmstuttgart.trackmaster.data.toDay
import de.hdmstuttgart.trackmaster.data.toDayAndMonth
import de.hdmstuttgart.trackmaster.data.toGermanDate

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate


/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TrackMasterUnitTest {

    private val statisticFragment = StatisticFragment()
    private val trackList = listOf(Track(distance = 2, time = 20), Track(distance = 5, time = 45), Track(distance = 12, time = 25))

    @Test
    fun testSumTracks() {
        assertEquals(19, statisticFragment.sumTracks(trackList))
    }

    @Test
    fun testToDay() {
        assertEquals("15.", toDay(LocalDate.of(2024, 2,15)))
        assertEquals("01.", toDay(LocalDate.of(2023, 10, 1)))
    }

    @Test
    fun testToGermanDate() {
        assertEquals("15.02.2024", toGermanDate(LocalDate.of(2024, 2,15)))
        assertEquals("01.10.2023", toGermanDate(LocalDate.of(2023, 10, 1)))
        assertEquals("05.03.1945", toGermanDate(LocalDate.of(1945, 3, 5)))
    }

    @Test
    fun testToDayAndMonth() {
        assertEquals("15.02.", toDayAndMonth(LocalDate.of(2024, 2,15)))
        assertEquals("01.10.", toDayAndMonth(LocalDate.of(2023, 10, 1)))
    }
}