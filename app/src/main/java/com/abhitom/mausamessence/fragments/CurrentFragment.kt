package com.abhitom.mausamessence.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
        if (DashBoard.isDataSaved){
            showData(DashBoard.data)
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
        if (!DashBoard.isCityFetched) {
            _binding?.txtLocation?.text = response.body()?.timezone
        }
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

        val userNameText="Hi, "+DashBoard.userName+"!"
        _binding?.txtUsername?.text=userNameText

        val humidity=response.body()?.current?.humidity.toString()+"%"
        val pressure=response.body()?.current?.pressure.toString()+" hPa"
        val visibility=response.body()?.current?.visibility.toString()+"m"
        _binding?.txtHumidity?.text= humidity
        _binding?.txtPressure?.text= pressure
        _binding?.txtVisiblity?.text= visibility
        _binding?.txtUv?.text= response.body()?.current?.uvi.toString()

        if (DashBoard.units=="metric"){
            val feelsLike=response.body()?.current?.feelsLike.toString()+" °C"
            val windSpeed=response.body()?.current?.windSpeed.toString()+" m/s"
            val minTemp= response.body()!!.daily?.get(0)?.temp?.min.toString() + " °C"
            val maxTemp= response.body()!!.daily?.get(0)?.temp?.max.toString() + " °C"
            _binding?.txtFeelsLike?.text= feelsLike
            _binding?.txtWindSpeed?.text= windSpeed
            _binding?.txtMinTemp?.text=minTemp
            _binding?.txtMaxTemp?.text=maxTemp
            _binding?.txtDegree?.text="°C"
        }else{
            val feelsLike=response.body()?.current?.feelsLike.toString()+" °F"
            val windSpeed=response.body()?.current?.windSpeed.toString()+" miles/s"
            val minTemp= response.body()!!.daily?.get(0)?.temp?.min.toString() + " °F"
            val maxTemp= response.body()!!.daily?.get(0)?.temp?.max.toString() + " °F"
            _binding?.txtFeelsLike?.text= feelsLike
            _binding?.txtWindSpeed?.text= windSpeed
            _binding?.txtMinTemp?.text=minTemp
            _binding?.txtMaxTemp?.text=maxTemp
            _binding?.txtDegree?.text="°F"
        }
    }

    //interface method
    override fun methodCurrent(response: Response<OneCallResponse>) {
        showData(response)
    }

    override fun methodCurrentCity(currentCity: String, b: Boolean) {
        if (b) {
            showCity(currentCity)
        }
    }

    private fun showCity(currentCity: String) {
        _binding?.txtLocation?.text = currentCity
    }
}

interface InterfaceCurrent {
    fun methodCurrent(response: Response<OneCallResponse>)
    fun methodCurrentCity(currentCity: String, b: Boolean)
}