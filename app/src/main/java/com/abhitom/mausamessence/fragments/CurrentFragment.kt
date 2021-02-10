package com.abhitom.mausamessence.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.databinding.FragmentCurrentBinding
import com.abhitom.mausamessence.retrofit.OneCallResponse
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CurrentFragment : Fragment(),InterfaceCurrent {

    private var _binding: FragmentCurrentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeBackground()
        checkTemp()
        if (DashBoard.isDataSaved){
            showData(DashBoard.data)
        }
    }

    private fun checkTemp() {
        if (DashBoard.units=="metric"){
            _binding?.txtDegree?.text="C"
        }else{
            _binding?.txtDegree?.text="F"
        }
    }

    // function to change background automatically wrt time
    private fun changeBackground() {
        val currentTime: Long = System.currentTimeMillis()
        val currentTimeDate = Date(currentTime)
        val currentTimeFormat = SimpleDateFormat("HH").format(currentTimeDate)

        when {
            (currentTimeFormat.toInt()>6) and (currentTimeFormat.toInt()<12) -> {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunrisehd) }
            }
            (currentTimeFormat.toInt()>=16) and (currentTimeFormat.toInt()<21) -> {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunsethd) }
            }
            (currentTimeFormat.toInt()>=12) and (currentTimeFormat.toInt()<16) -> {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.noonhd) }
            }
            else -> {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.nighthd) }
            }
        }
    }

    // function to display data
    private fun showData(response: Response<OneCallResponse>) {
        Log.i("TAGGER","SHOW DATA")
        _binding?.txtLocation?.text=response.body()?.timezone
        _binding?.txtTemp?.text= response.body()?.current?.temp.toString()
        _binding?.txtWeather?.text= response.body()?.current?.weather?.get(0)?.main
        val sunrise: Long = response.body()?.current?.sunrise?.let { java.lang.Long.valueOf(it) }!! * 1000
        val sunrisedf = Date(sunrise)
        val sunrisevv = SimpleDateFormat("hh:mm a").format(sunrisedf)
        _binding?.txtSunrise?.text=sunrisevv
        val sunset: Long = response.body()?.current?.sunset?.let { java.lang.Long.valueOf(it) }!! * 1000
        val sunsetdf = Date(sunset)
        val sunsetvv = SimpleDateFormat("hh:mm a").format(sunsetdf)
        _binding?.txtSunset?.text=sunsetvv
        _binding?.txtUsername?.text="Hi, "+DashBoard.userName
        if (DashBoard.units=="metric"){
            val feelsLike=response.body()?.current?.feelsLike.toString()+" °C"
            val windSpeed=response.body()?.current?.windSpeed.toString()+" m/s"
            _binding?.txtFeelsLike?.text= feelsLike
            _binding?.txtHumidity?.text= response.body()?.current?.humidity.toString()
            _binding?.txtPressure?.text= response.body()?.current?.pressure.toString()
            _binding?.txtVisiblity?.text= response.body()?.current?.visibility.toString()
            _binding?.txtWindSpeed?.text= windSpeed
            _binding?.txtUv?.text= response.body()?.current?.uvi.toString()
            val minTemp= response.body()!!.daily?.get(0)?.temp?.min.toString() + " °C"
            val maxTemp= response.body()!!.daily?.get(0)?.temp?.max.toString() + " °C"
            _binding?.txtMinTemp?.text=minTemp
            _binding?.txtMaxTemp?.text=maxTemp
        }else{
            val feelsLike=response.body()?.current?.feelsLike.toString()+" °F"
            val windSpeed=response.body()?.current?.windSpeed.toString()+" miles/s"
            _binding?.txtFeelsLike?.text= feelsLike
            _binding?.txtHumidity?.text= response.body()?.current?.humidity.toString()
            _binding?.txtPressure?.text= response.body()?.current?.pressure.toString()
            _binding?.txtVisiblity?.text= response.body()?.current?.visibility.toString()
            _binding?.txtWindSpeed?.text= windSpeed
            _binding?.txtUv?.text= response.body()?.current?.uvi.toString()
            val minTemp= response.body()!!.daily?.get(0)?.temp?.min.toString() + " °F"
            val maxTemp= response.body()!!.daily?.get(0)?.temp?.max.toString() + " °F"
            _binding?.txtMinTemp?.text=minTemp
            _binding?.txtMaxTemp?.text=maxTemp
        }
    }

    override fun methodCurrent(response: Response<OneCallResponse>) {
        showData(response)
    }
}

interface InterfaceCurrent {
    fun methodCurrent(response: Response<OneCallResponse>)
}