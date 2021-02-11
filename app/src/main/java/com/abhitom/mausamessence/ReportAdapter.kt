package com.abhitom.mausamessence

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhitom.mausamessence.databinding.OneDayCardviewBinding
import com.abhitom.mausamessence.retrofit.OneDay


class ReportAdapter(private val list: MutableList<OneDay>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(private val itemBinding: OneDayCardviewBinding) : RecyclerView.ViewHolder
    (itemBinding.root){
        fun bind(obj: OneDay){
            itemBinding.tvDay.text = obj.Dday
            itemBinding.tvDate.text = obj.Ddate
            if (DashBoard.units=="metric"){
                val windSpeed=obj.WindSpeed+" m/s"
                itemBinding.tvWindSpeed.text = windSpeed
                val minTemp= obj.minTemp+ " °C"
                val maxTemp= obj.maxTemp+ " °C"
                itemBinding.tvMin.text =minTemp
                itemBinding.tvMax.text =maxTemp
            }else{
                val windSpeed=obj.WindSpeed+" miles/s"
                itemBinding.tvWindSpeed.text = windSpeed
                val minTemp= obj.minTemp+ " °F"
                val maxTemp= obj.maxTemp+ " °F"
                itemBinding.tvMin.text =minTemp
                itemBinding.tvMax.text =maxTemp
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemBinding = OneDayCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val obj = list[position]
        holder.bind(obj)
    }

    override fun getItemCount(): Int = list.size
}