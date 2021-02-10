package com.abhitom.mausamessence

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.abhitom.mausamessence.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        prefs=this.getSharedPreferences(
                getString(R.string.parent_package_name),
                Context.MODE_PRIVATE
        )

        checkPermissions()

        btnMAStartOnClickListener()
    }

    private fun btnMAStartOnClickListener() {
        binding.btnMAStart.setOnClickListener {
            val name=prefs.getString("Name", "")
            if(name=="") {
                val intent= Intent(this, UserActivity::class.java)
                intent.clearStack()
                startActivity(intent)
            }else{
                val intent= Intent(this, DashBoard::class.java)
                intent.clearStack()
                startActivity(intent)
            }
        }
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ), 101
            )
            return
        }else{
            enableGps()
        }
    }

    private fun enableGps() {

        val mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)
                .setFastestInterval(1000)

        val settingsBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)

        val result =
                LocationServices.getSettingsClient(this).checkLocationSettings(settingsBuilder.build())
        result.addOnCompleteListener { task ->
            //getting the status code from exception
            try {
                task.getResult(ApiException::class.java)
            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        val resolvableApiException = ex as ResolvableApiException
                        resolvableApiException.startResolutionForResult(
                                this, 101
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Toast.makeText(
                                this,
                                "PendingIntent unable to execute request.",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Toast.makeText(
                                this,
                                "Something is wrong in your GPS",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAGGER", "coarse location permission granted")
                    locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                    if (!hasGps || !hasNetwork) {
                        enableGps()
                    }
                    btnMAStartOnClickListener()
                } else {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        }
    }

    fun Intent.clearStack(additionalFlags: Int = 0) {
        flags = additionalFlags or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
}