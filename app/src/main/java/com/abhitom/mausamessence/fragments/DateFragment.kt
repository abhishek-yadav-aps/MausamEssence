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
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.databinding.FragmentDateBinding
import com.abhitom.mausamessence.retrofit.OneCallResponse
import com.abhitom.mausamessence.retrofit.RetroFitClient
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    val mumbaiLoc= LatLng(19.01441,72.847939)
    val noidaLoc= LatLng(28.496149,77.536011)
    val delhiLoc= LatLng(28.666668,77.216667)

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
        HideThem()

        binding?.txtUsername1!!.text="Hi, "+DashBoard.userName

        val cityArrayAdapter = ArrayAdapter<String>(context!!, R.layout.spinner_text_item, cities)
        binding?.spinCity?.adapter = cityArrayAdapter
        binding?.spinCity?.onItemSelectedListener = this
        binding?.spinCity?.setSelection(0, false)

        val currentDate: Long = java.lang.Long.valueOf(System.currentTimeMillis())
        val currentDatedf = Date(currentDate)
        val currentDatevv = SimpleDateFormat("dd-MM-yyyy").format(currentDatedf)
        binding?.tvDFDate?.text=currentDatevv

        val currentdd = SimpleDateFormat("dd").format(currentDatedf)
        val currentmm = SimpleDateFormat("MM").format(currentDatedf)
        val currentyy= SimpleDateFormat("yyyy").format(currentDatedf)
        dayOfMonthsaver=currentdd.toInt()
        monthsaver=currentmm.toInt()-1
        yearsaver=currentyy.toInt()

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

    private fun getData(loc: LatLng) {
        RetroFitClient.instance.service.oneCallApi(loc.latitude, loc.longitude, DashBoard.apiKey, DashBoard.units)
            .enqueue(object : Callback<OneCallResponse> {
                override fun onResponse(
                    call: Call<OneCallResponse>,
                    response: Response<OneCallResponse>
                ) {
                    if (response.isSuccessful) {
                        showData(response)
                    } else {
                        toastMaker(response.errorBody().toString(), DashBoard.dateFragment)
                    }
                }

                override fun onFailure(call: Call<OneCallResponse>, t: Throwable) {
                    toastMaker("No Internet / Server Down", DashBoard.dateFragment)
                }
            })

    }

    private fun toastMaker(message: String?, Fragment: Fragment) {
        if (Fragment.isVisible) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun btnSearchOnClickListener() {
        binding?.btnSearch?.setOnClickListener {
            when(currentCity){
                0 -> {
                    HideThem()
                }
                1 ->{
                    getData(delhiLoc)
                    ShowThem()
                }
                2->{
                    getData(mumbaiLoc)
                    ShowThem()
                }
                3->{
                    getData(noidaLoc)
                    ShowThem()
                }
            }
        }
    }

    private fun showData(response: Response<OneCallResponse>) {
        for (i in response.body()?.daily?.indices!!){
            val sunrise: Long = response.body()?.daily!![i]?.dt.let { java.lang.Long.valueOf(it!!) }!! * 1000
            val sunrisedf = Date(sunrise)
            val sunrisedd = SimpleDateFormat("dd").format(sunrisedf)
            val sunriseMM = SimpleDateFormat("MM").format(sunrisedf)
            val sunriseyy = SimpleDateFormat("yyyy").format(sunrisedf)
            if (sunrisedd.toInt()==dayOfMonthsaver && sunriseMM.toInt()==(monthsaver+1) && sunriseyy.toInt()==yearsaver){

                binding?.txtTemp?.text= response.body()?.current?.temp.toString()
                binding?.txtWeather?.text= response.body()?.current?.weather?.get(0)?.main
                val sunris: Long = response.body()?.current?.sunrise?.let { java.lang.Long.valueOf(it) }!! * 1000
                val sunrisdf = Date(sunris)
                val sunrisvv = SimpleDateFormat("hh:mm a").format(sunrisdf)
                binding?.txtSunrise?.text=sunrisvv
                val sunset: Long = response.body()?.current?.sunset?.let { java.lang.Long.valueOf(it) }!! * 1000
                val sunsetdf = Date(sunset)
                val sunsetvv = SimpleDateFormat("hh:mm a").format(sunsetdf)
                binding?.txtSunset?.text=sunsetvv
                if (DashBoard.units=="metric"){
                    binding?.txtDegree?.text="C"
                    binding?.txtFeelsLike?.text= response.body()?.current?.feelsLike.toString()+" °C"
                    binding?.txtHumidity?.text= response.body()?.current?.humidity.toString()
                    binding?.txtPressure?.text= response.body()?.current?.pressure.toString()
                    binding?.txtVisiblity?.text= response.body()?.current?.visibility.toString()
                    binding?.txtWindSpeed?.text= response.body()?.current?.windSpeed.toString()+" m/s"
                    binding?.txtUv?.text= response.body()?.current?.uvi.toString()
                    val minTemp= response.body()!!.daily?.get(0)?.temp?.min.toString() + " °C"
                    val maxTemp= response.body()!!.daily?.get(0)?.temp?.max.toString() + " °C"
                    binding?.txtDateMin?.text=minTemp
                    binding?.txtDateMax?.text=maxTemp
                }else{
                    binding?.txtDegree?.text="F"
                    binding?.txtFeelsLike?.text= response.body()?.current?.feelsLike.toString()+" °F"
                    binding?.txtHumidity?.text= response.body()?.current?.humidity.toString()
                    binding?.txtPressure?.text= response.body()?.current?.pressure.toString()
                    binding?.txtVisiblity?.text= response.body()?.current?.visibility.toString()
                    binding?.txtWindSpeed?.text= response.body()?.current?.windSpeed.toString()+" miles/s"
                    binding?.txtUv?.text= response.body()?.current?.uvi.toString()
                    val minTemp= response.body()!!.daily?.get(0)?.temp?.min.toString() + " °F"
                    val maxTemp= response.body()!!.daily?.get(0)?.temp?.max.toString() + " °F"
                    binding?.txtDateMin?.text=minTemp
                    binding?.txtDateMax?.text=maxTemp
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