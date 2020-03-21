package khoina.weatherforecast.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import khoina.weatherforecast.ForecastModel
import java.text.SimpleDateFormat

class ForecastAdapter: RecyclerView.Adapter<ForecastViewHolder>() {

    private val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy")
    private val items = mutableListOf<ForecastModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder.createViewHolder(
            parent,
            dateFormatter
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitData(testData: List<ForecastModel>) {
        items.clear()
        items.addAll(testData)
        notifyDataSetChanged()
    }
}