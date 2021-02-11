package com.abhitom.mausamessence.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.ReportAdapter
import com.abhitom.mausamessence.databinding.FragmentReportBinding
import com.abhitom.mausamessence.retrofit.OneDay
import com.google.android.gms.maps.model.Dash
import java.text.SimpleDateFormat
import java.util.*


class ReportFragment : Fragment(),InterfaceReport {

    private var binding: FragmentReportBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeBackground()
        val userNameText="Hi, "+DashBoard.userName+"!"
        binding?.txtUsername?.text=userNameText
        if (DashBoard.isDataSaved) {
            if (!DashBoard.isCityFetched) {
                binding?.txtLocation?.text = DashBoard.data.body()?.timezone
            }
        }
        if (DashBoard.isCityFetched){
            binding?.txtLocation?.text = DashBoard.currentCity
        }

        if (DashBoard.isReportDone){
            binding?.rv7Day?.layoutManager = LinearLayoutManager(context, LinearLayoutManager
                .HORIZONTAL, false)
            binding?.rv7Day?.itemAnimator = DefaultItemAnimator()
            val adapter = ReportAdapter(DashBoard.reportList)
            binding?.rv7Day?.adapter = adapter
        }
    }

    private fun changeBackground() {
        val currentTime: Long = System.currentTimeMillis()
        val currentTimeDate = Date(currentTime)
        val currentTimeFormat = SimpleDateFormat("HH").format(currentTimeDate)

        when {
            (currentTimeFormat.toInt()>6) and (currentTimeFormat.toInt()<12) -> {
                if (DashBoard.reportFragment.isVisible) {
                    binding?.clReportLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunrisehd) }
                }
            }
            (currentTimeFormat.toInt()>=16) and (currentTimeFormat.toInt()<21) -> {
                if (DashBoard.reportFragment.isVisible) {
                    binding?.clReportLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunsethd) }
                }
            }
            (currentTimeFormat.toInt()>=12) and (currentTimeFormat.toInt()<16) -> {
                if (DashBoard.reportFragment.isVisible) {
                    binding?.clReportLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.noonhd) }
                }
            }
            else -> {
                if (DashBoard.reportFragment.isVisible) {
                    binding?.clReportLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.nighthd) }
                }
            }
        }
    }

    override fun methodReport(list: MutableList<OneDay>) {
        binding?.rv7Day?.layoutManager = LinearLayoutManager(context, LinearLayoutManager
            .HORIZONTAL, false)
        binding?.rv7Day?.itemAnimator = DefaultItemAnimator()
        val adapter = ReportAdapter(list)
        binding?.rv7Day?.adapter = adapter
    }
}

interface InterfaceReport {
    fun methodReport(list:MutableList<OneDay>)
}
