package khoina.weatherforecast

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import khoina.weatherforecast.view.ForecastActivity
import khoina.weatherforecast.view.ForecastAdapter
import okhttp3.OkHttpClient
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@LargeTest
class UiTest {

    val application = ApplicationProvider.getApplicationContext<MainApp>()

    @get:Rule
    var activityRule = ActivityTestRule(ForecastActivity::class.java)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    lateinit var networkIdlingResource: OkHttp3IdlingResource

    init {
        okHttpClient = application.getAppComponent().okHttpClient()
    }

    @Before
    fun setup() {
        networkIdlingResource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(networkIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(networkIdlingResource)
    }

    @Test
    fun uiTest_isCorrectInitButtonTextElements() {
        onView(withId(R.id.etPlace)).check(matches(withText("")))
        onView(withId(R.id.etPlace)).check(matches(hasTextColor(R.color.place_query_text)))
        onView(withId(R.id.pbLoading)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.tvError)).check(matches(withText("")))
        onView(withId(R.id.tvError)).check(matches(hasTextColor(R.color.error_text)))
    }

    @Test
    fun uiTest_isCorrectInitListingLayout() {
        onView(withId(R.id.rcvWeatherForecast)).check(matches(isAssignableFrom(RecyclerView::class.java)))
        onView(withId(R.id.rcvWeatherForecast)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.rcvWeatherForecast)).check(matches(hasSibling(withId(R.id.btGetWeather))))
        onView(withId(R.id.rcvWeatherForecast)).check(matches(hasSibling(withId(R.id.pbLoading))))
        onView(withId(R.id.rcvWeatherForecast)).check(matches(hasSibling(withId(R.id.etPlace))))
        onView(withId(R.id.rcvWeatherForecast)).check(matches(hasChildCount(0)))

        val rcvForecastList = activityRule.activity.findViewById<RecyclerView>(R.id.rcvWeatherForecast)
        assertTrue(rcvForecastList.layoutManager is LinearLayoutManager)
        val layoutManager = rcvForecastList.layoutManager as LinearLayoutManager
        assertEquals(layoutManager.orientation, RecyclerView.VERTICAL)
    }

    @Test
    fun uiTest_isCorrectViewStatesOnGettingWeather() {
        onView(withId(R.id.etPlace)).perform(typeText("saigon"), closeSoftKeyboard())
        onView(withId(R.id.pbLoading)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.tvError)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.tvError)).check(matches(withText("")))
        onView(withId(R.id.btGetWeather)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.pbLoading)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.tvError)).check(matches(withText("")))
    }

    @Test
    fun uiTest_isCorrectItemCountOfSaigonWeather() {
        onView(withId(R.id.etPlace)).perform(typeText("saigon"), closeSoftKeyboard())
        onView(withId(R.id.btGetWeather)).perform(click())
        Thread.sleep(2000)
        val rcvForecastList = activityRule.activity.findViewById<RecyclerView>(R.id.rcvWeatherForecast)
        val layoutManager = rcvForecastList.layoutManager as LinearLayoutManager
        val adapter = rcvForecastList.adapter as ForecastAdapter
        assertEquals(adapter.itemCount, API_ITEM_COUNT)
        val firstItemPos = layoutManager.findFirstCompletelyVisibleItemPosition()
        assertEquals(firstItemPos, 0)
    }

    @Test
    fun uiTest_isCorrectDisplayingListingItemsOfSaigonWeather() {
        onView(withId(R.id.etPlace)).perform(typeText("saigon"), closeSoftKeyboard())
        onView(withId(R.id.btGetWeather)).perform(click())
        Thread.sleep(2000)

        val rcvForecastList = activityRule.activity.findViewById<RecyclerView>(R.id.rcvWeatherForecast)
        val layoutManager = rcvForecastList.layoutManager as LinearLayoutManager
        val adapter = rcvForecastList.adapter as ForecastAdapter
        val itemViewAssertionBlock: (first: Int, last: Int) -> Unit = { first, last ->
            for (index in first..last) {
                val itemData = adapter.getItem(index)
                val childView = rcvForecastList.getChildAt(index)
                assertTrue(childView is TextView)

                val tvForecastInfo = childView as TextView
                val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy")
                val forecastInfo = "Date: ${dateFormatter.format(itemData.date)}\n" +
                        "Average temperature: ${itemData.aveTemp.toInt()}\u2103\n" +
                        "Pressure: ${itemData.pressure}\n" +
                        "Humidity: ${itemData.humidity}%\n" +
                        "Description: ${itemData.description}"
                assertEquals(tvForecastInfo.text, forecastInfo)
            }
        }
        var firstItemPos = layoutManager.findFirstCompletelyVisibleItemPosition()
        var lastItemPos = layoutManager.findLastCompletelyVisibleItemPosition()
        val diff = lastItemPos - firstItemPos
        while (lastItemPos < API_ITEM_COUNT) {
            itemViewAssertionBlock(firstItemPos, lastItemPos)
            firstItemPos = lastItemPos + 1
            lastItemPos = firstItemPos + diff
            onView(withId(R.id.rcvWeatherForecast)).perform(scrollToPosition<RecyclerView.ViewHolder>(lastItemPos))
        }
    }

    @Test
    fun uiTest_isCorrectDisplayingErrorOfCityNotFound() {
        onView(withId(R.id.etPlace)).perform(typeText("foo_city"), closeSoftKeyboard())
        onView(withId(R.id.btGetWeather)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.pbLoading)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.tvError)).check(matches(withText("city not found")))

        val rcvForecastList = activityRule.activity.findViewById<RecyclerView>(R.id.rcvWeatherForecast)
        val layoutManager = rcvForecastList.layoutManager as LinearLayoutManager
        val adapter = rcvForecastList.adapter as ForecastAdapter
        assertEquals(adapter.itemCount, 0)
        assertEquals(rcvForecastList.childCount, 0)
    }

    @Test
    fun uiTest_isCorrectDisplayingErrorOfEmptyPlace() {
        onView(withId(R.id.etPlace)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.btGetWeather)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.pbLoading)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.tvError)).check(matches(withText("Nothing to geocode")))

        val rcvForecastList = activityRule.activity.findViewById<RecyclerView>(R.id.rcvWeatherForecast)
        val layoutManager = rcvForecastList.layoutManager as LinearLayoutManager
        val adapter = rcvForecastList.adapter as ForecastAdapter
        assertEquals(adapter.itemCount, 0)
        assertEquals(rcvForecastList.childCount, 0)
    }

    companion object {
        const val API_ITEM_COUNT = 7
    }
}