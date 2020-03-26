package khoina.weatherforecast.view

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import khoina.weatherforecast.ForecastModel
import java.text.SimpleDateFormat

class ForecastAdapter: RecyclerView.Adapter<ForecastViewHolder>() {

    private val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy")

    private val diffUtil = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder.createViewHolder(
            parent,
            dateFormatter
        )
    }

    override fun getItemCount() = diffUtil.currentList.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun submitData(data: List<ForecastModel>) {
        diffUtil.submitList(data)
    }

    fun getItem(itemPos: Int) = diffUtil.currentList[itemPos]

    object DIFF_CALLBACK: DiffUtil.ItemCallback<ForecastModel>() {
        override fun areItemsTheSame(oldItem: ForecastModel, newItem: ForecastModel): Boolean {
            return oldItem.place == newItem.place && oldItem.date.time == newItem.date.time
        }

        override fun areContentsTheSame(oldItem: ForecastModel, newItem: ForecastModel): Boolean {
            return oldItem == newItem
        }

    }
}