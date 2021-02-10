package com.abhitom.mausamessence

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.abhitom.mausamessence.databinding.ActivityDashBoardBinding
import com.abhitom.mausamessence.fragments.CurrentFragment
import com.abhitom.mausamessence.fragments.DateFragment
import com.abhitom.mausamessence.fragments.ReportFragment
import com.abhitom.mausamessence.fragments.SettingsFragment


class DashBoard : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var locationGps:Location
    private lateinit var locationNetwork:Location
    lateinit var finalLocation:Location

    companion object{
        var currentFragment= CurrentFragment()
        var reportFragment= ReportFragment()
        var dateFragment= DateFragment()
        var settingsFragment= SettingsFragment()
        lateinit var locationToShare:Location
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding= ActivityDashBoardBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        getLocation()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

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
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 101)
                    return
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100F, object :
                    LocationListener {
                    override fun onLocationChanged(p0: Location) {
                        finalLocation = p0
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
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

}