package khoina.weatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_date_forecast.*
import java.text.DateFormat

class ForecastViewHolder(
    override val containerView: View,
    private val dateFormatter: DateFormat
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    lateinit var boundData: ForecastModel

    fun bind(data: ForecastModel) {
        boundData = data

        val context = itemView.context
        val forecastInfo = "${context.getString(R.string.label_date)}: ${dateFormatter.format(data.date)}\n" +
                "${context.getString(R.string.label_avg_temp)}: ${data.aveTemp.toInt()}\u2103\n" +
                "${context.getString(R.string.label_pressure)}: ${data.pressure}\n" +
                "${context.getString(R.string.label_humidity)}: ${data.humidity}%\n" +
                "${context.getString(R.string.label_description)}: ${data.description}"

        tvForecastInfo.text = forecastInfo
    }

    companion object {
        fun createViewHolder(parent: ViewGroup, dateFormatter: DateFormat): ForecastViewHolder {
            return ForecastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_date_forecast, parent, false), dateFormatter)
        }
    }
}
