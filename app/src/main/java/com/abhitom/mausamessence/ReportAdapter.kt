package com.abhitom.mausamessence

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhitom.mausamessence.databinding.OneDayCardviewBinding
import com.abhitom.mausamessence.retrofit.OneDay


class ReportAdapter(val list: MutableList<OneDay>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(private val itemBinding: OneDayCardviewBinding) : RecyclerView.ViewHolder
    (itemBinding.root){
        fun bind(obj: OneDay){
            itemBinding.textMin.text = obj.minTemp
            itemBinding.txtMax.text = obj.maxTemp
            itemBinding.txtDay.text = obj.Dday
            itemBinding.txtDate.text = obj.Ddate
            itemBinding.txtSunrise.text = obj.Sunrise
            itemBinding.txtSunset.text = obj.Sunset
            itemBinding.textWindSpeed.text = obj.WindSpeed
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