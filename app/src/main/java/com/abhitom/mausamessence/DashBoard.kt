package com.abhitom.mausamessence

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.abhitom.mausamessence.databinding.ActivityDashBoardBinding
import com.abhitom.mausamessence.fragments.CurrentFragment
import com.abhitom.mausamessence.fragments.DateFragment
import com.abhitom.mausamessence.fragments.ReportFragment
import com.abhitom.mausamessence.fragments.SettingsFragment
import retrofit2.Response

class DashBoard : AppCompatActivity() {

    private lateinit var binding: ActivityDashBoardBinding

    companion object{
        var currentFragment= CurrentFragment()
        var reportFragment= ReportFragment()
        var dateFragment= DateFragment()
        var settingsFragment= SettingsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding= ActivityDashBoardBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

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
}