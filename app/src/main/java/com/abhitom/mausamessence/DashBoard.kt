package com.abhitom.mausamessence

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.abhitom.mausamessence.databinding.ActivityDashBoardBinding
import com.abhitom.mausamessence.fragments.*
import com.abhitom.mausamessence.retrofit.OneCallResponse
import com.abhitom.mausamessence.retrofit.OneDay
import com.abhitom.mausamessence.retrofit.RetroFitClient
import com.abhitom.mausamessence.retrofit.ReverseGeoCodingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class DashBoard : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var locationGps:Location
    private lateinit var locationNetwork:Location
    lateinit var finalLocation:Location
    private var listenerCurrent: InterfaceCurrent? = null
    private var listenerReport: InterfaceReport? = null
    private lateinit var prefs: SharedPreferences

    private fun setListenerCurrent(listener: InterfaceCurrent?) {
        this.listenerCurrent = listener
    }
    private fun setListenerReport(listenerReport: InterfaceReport?) {
        this.listenerReport = listenerReport
    }

    companion object{
        var currentFragment= CurrentFragment()
        var reportFragment= ReportFragment()
        var dateFragment= DateFragment()
        var settingsFragment= SettingsFragment()
        lateinit var locationToShare:Location
        var units="metric"
        var isDataSaved=false
        lateinit var data: Response<OneCallResponse>
        var reportList: MutableList<OneDay> = mutableListOf()
        var isReportDone=false
        var isCityFetched=false
        var apiKey="22147f19dfcc656710c95cabb152527a"
        var userName=""
        var currentCity=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding= ActivityDashBoardBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        getLocation()

        prefs=this.getSharedPreferences(
            getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )
        userName= prefs.getString("Name", "").toString()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        setListenerCurrent(currentFragment)
        setListenerReport(reportFragment)

        setCurrentFragment(currentFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.C7Day -> setCurrentFragment(reportFragment)
                R.id.CCurrent -> setCurrentFragment(currentFragment)
                R.id.CDate -> setCurrentFragment(dateFragment)
                R.id.CSettings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.placehoder_fragment, fragment)
            commit()
        }

    //function to fetch data from api
    fun getData(finalLocation: Location) {

        RetroFitClient.instance.service.oneCallApi(finalLocation.latitude, finalLocation.longitude, apiKey, units)
            .enqueue(object : Callback<OneCallResponse> {
                override fun onResponse(
                    call: Call<OneCallResponse>,
                    response: Response<OneCallResponse>
                ) {

                    if (response.isSuccessful) {

                        isDataSaved = true
                        data = response
                        listenerCurrent?.methodCurrent(response)
                        getDataForReport(response)
                    } else {

                        toastMaker(response.errorBody().toString(),currentFragment)
                    }
                }

                override fun onFailure(call: Call<OneCallResponse>, t: Throwable) {
                    toastMaker("No Internet / Server Down", currentFragment)
                }
            })

        RetroFitClient.instance.service.reverseGeoCoding(finalLocation.latitude, finalLocation.longitude, apiKey, 1)
            .enqueue(object : Callback<List<ReverseGeoCodingResponse>> {
                override fun onResponse(
                    call: Call<List<ReverseGeoCodingResponse>>,
                    response: Response<List<ReverseGeoCodingResponse>>
                ) {

                    if (response.isSuccessful) {
                        if(response.body()!!.isNotEmpty()) {
                            currentCity =
                                response.body()!![0].name + ", " + response.body()!![0].country
                            listenerCurrent?.methodCurrentCity(currentCity,true)
                            isCityFetched=true
                        }else{
                            listenerCurrent?.methodCurrentCity("", false)
                            isCityFetched=false
                        }
                    } else {

                        toastMaker(response.errorBody().toString(),currentFragment)
                    }
                }

                override fun onFailure(call: Call<List<ReverseGeoCodingResponse>>, t: Throwable) {
                    toastMaker("No Internet / Server Down", currentFragment)
                }
            })
    }

    private fun getDataForReport(response: Response<OneCallResponse>) {
        val list: MutableList<OneDay> = mutableListOf()

        for (i in 0 until response.body()?.daily?.size!!){
            val day: Long = response.body()?.daily!![i]?.dt.let { it?.let { it1 -> java.lang.Long.valueOf(it1) } }!! * 1000
            val daydf = Date(day)
            val dayOfWeek = SimpleDateFormat("EEEE").format(daydf)
            val date = SimpleDateFormat("dd/MM").format(daydf)
            val sunrise: Long = response.body()?.daily!![i]?.sunrise.let { it?.let { it1 -> java.lang.Long.valueOf(it1) } }!! * 1000
            val sunrisedf = Date(sunrise)
            val sunset: Long = response.body()?.daily!![i]?.sunset.let { it?.let { it1 -> java.lang.Long.valueOf(it1) } }!! * 1000
            val sunsetdf = Date(sunset)
            val sunrisevv = SimpleDateFormat("hh:mm a").format(sunrisedf)
            val sunsetvv = SimpleDateFormat("hh:mm a").format(sunsetdf)
            list.add(OneDay(dayOfWeek, date, sunrisevv, sunsetvv, response.body()!!.daily?.get(i)?.temp?.max.toString(), response.body()!!.daily?.get(i)?.temp?.min.toString(), response.body()!!.daily?.get(i)?.windSpeed.toString() ))
        }
        isReportDone=true
        reportList=list
        listenerReport?.methodReport(list)
    }

    //function to find user current location (NETWORK + GPS)
    fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps || hasNetwork) {

            if (hasGps) {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
                    return
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100F, object :
                    LocationListener {
                    override fun onLocationChanged(p0: Location) {
                        finalLocation = p0
                        getData(finalLocation)
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String?) {}

                    override fun onProviderDisabled(provider: String?) {}

                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100F, object :
                    LocationListener {
                    override fun onLocationChanged(p0: Location) {
                        finalLocation = p0
                        getData(finalLocation)
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String?) {}

                    override fun onProviderDisabled(provider: String?) {}

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(this@DashBoard::locationGps.isInitialized && this@DashBoard::locationNetwork.isInitialized)
                finalLocation = if(locationGps.accuracy > locationNetwork.accuracy){
                    locationGps
                }else{
                    locationNetwork
                }
            if(this::finalLocation.isInitialized) {
                locationToShare=finalLocation
                getData(finalLocation)
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun toastMaker(message: String?, Fragment: Fragment) {
        if (Fragment.isVisible) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

}