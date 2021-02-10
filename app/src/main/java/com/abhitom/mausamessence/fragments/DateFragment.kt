package com.abhitom.mausamessence.fragments

import android.app.DatePickerDialog
import android.content.ContentValues
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.databinding.FragmentDateBinding
import java.text.SimpleDateFormat
import java.util.*

class DateFragment : Fragment(),DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private var monthsaver: Int=0
    private var yearsaver: Int=0
    var dayOfMonthsaver: Int=0
    private var binding: FragmentDateBinding? = null
    lateinit var datePickerDialog: DatePickerDialog
    private var cities:MutableList<String> = mutableListOf()
    var currentCity = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDateBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeBackground()

        cities.clear()
        cities.add("Select City..")
        cities.add("Delhi")
        cities.add("Mumbai")
        cities.add("Noida")
        if(currentCity == 0){
            HideThem()
        }

        val cityArrayAdapter = ArrayAdapter<String>(context!!, R.layout.spinner_text_item, cities)
        binding?.spinCity?.adapter = cityArrayAdapter
        binding?.spinCity?.onItemSelectedListener = this
        binding?.spinCity?.setSelection(0, false)

        btnSearchOnClickListener()

        tvDFDateOnClickListener()
    }

    private fun tvDFDateOnClickListener() {
        val currentDate: Long = java.lang.Long.valueOf(System.currentTimeMillis())
        val currentDatedf = Date(currentDate)
        val currentDatevv = SimpleDateFormat("dd-MM-yyyy").format(currentDatedf)
        binding?.tvDFDate?.text=currentDatevv

        binding?.tvDFDate?.setOnClickListener {
            datePickerDialog=
                DatePickerDialog(activity!!, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(
                    Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate= Calendar.getInstance().timeInMillis
            val c= Calendar.getInstance()
            c.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 7)
            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun btnSearchOnClickListener() {
        binding?.btnSearch?.setOnClickListener {
            when(currentCity){
                0 -> {
                    HideThem()
                }
                1 -> {
                    ShowThem()
                }
                2->{
                    ShowThem()
                }
                3->{
                    ShowThem()
                }
            }
        }
    }

    private fun ShowThem(){
        binding?.cardView?.visibility = View.VISIBLE
        binding?.txtWeather?.visibility = View.VISIBLE
        binding?.txtDegree?.visibility = View.VISIBLE
        binding?.txtTemp?.visibility = View.VISIBLE
        binding?.txtDateMax?.visibility = View.VISIBLE
        binding?.txtDateMin?.visibility = View.VISIBLE
    }

    private fun HideThem(){
        binding?.cardView?.visibility = View.INVISIBLE
        binding?.txtWeather?.visibility = View.INVISIBLE
        binding?.txtDegree?.visibility = View.INVISIBLE
        binding?.txtTemp?.visibility = View.INVISIBLE
        binding?.txtDateMax?.visibility = View.INVISIBLE
        binding?.txtDateMin?.visibility = View.INVISIBLE
    }

    private fun changeBackground() {
        val currentTime: Long = System.currentTimeMillis()
        val currentTimeDate = Date(currentTime)
        val currentTimeFormat = SimpleDateFormat("HH").format(currentTimeDate)

        when {
            (currentTimeFormat.toInt()>6) and (currentTimeFormat.toInt()<12) -> {
                if (DashBoard.dateFragment.isVisible) {
                    binding?.clDFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunrisehd) }
                }
            }
            (currentTimeFormat.toInt()>=16) and (currentTimeFormat.toInt()<21) -> {
                if (DashBoard.dateFragment.isVisible) {
                    binding?.clDFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunsethd) }
                }
            }
            (currentTimeFormat.toInt()>=12) and (currentTimeFormat.toInt()<16) -> {
                if (DashBoard.dateFragment.isVisible) {
                    binding?.clDFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.noonhd) }
                }
            }
            else -> {
                if (DashBoard.dateFragment.isVisible) {
                    binding?.clDFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.nighthd) }
                }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding?.tvDFDate?.text=dayOfMonth.toString()+"-"+(month+1).toString()+"-"+year.toString()
        dayOfMonthsaver=dayOfMonth
        yearsaver=year
        monthsaver=month
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.id == R.id.spin_city){
            currentCity = id.toInt()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}