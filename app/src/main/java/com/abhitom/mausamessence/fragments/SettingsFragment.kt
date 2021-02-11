package com.abhitom.mausamessence.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.databinding.FragmentSettingsBinding
import java.text.SimpleDateFormat
import java.util.*


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    lateinit var prefs:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toggleButtonCheckListener()

        changeBackground()

        if (DashBoard.settingsFragment.isVisible) {
            prefs = context!!.getSharedPreferences(
                getString(R.string.parent_package_name),
                Context.MODE_PRIVATE
            )
        }
        val userNameText="Hi, "+DashBoard.userName
        _binding?.txtUsername!!.text=userNameText
        _binding?.textInputUserName?.editText?.setText(DashBoard.userName)
        _binding?.btnSFSave?.setOnClickListener {
            if (!_binding!!.textInputUserName.editText?.text.isNullOrEmpty() || !_binding!!.textInputUserName.editText?.text.isNullOrBlank()) {
                DashBoard.userName = _binding!!.textInputUserName.editText?.text.toString()
                _binding!!.txtUsername.text="Hi, "+DashBoard.userName
                _binding?.textInputUserName?.editText?.setText(_binding!!.textInputUserName.editText?.text.toString())
                prefs.edit()
                    .putString("Name", _binding!!.textInputUserName.editText?.text.toString())
                    .apply()
            }
        }
    }

    private fun changeBackground() {
        val currentTime: Long = System.currentTimeMillis()
        val currentTimeDate = Date(currentTime)
        val currentTimeFormat = SimpleDateFormat("HH").format(currentTimeDate)

        when {
            (currentTimeFormat.toInt()>6) and (currentTimeFormat.toInt()<12) -> {
                if (DashBoard.settingsFragment.isVisible) {
                    _binding?.clSFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunrisehd) }
                }
            }
            (currentTimeFormat.toInt()>=16) and (currentTimeFormat.toInt()<21) -> {
                if (DashBoard.settingsFragment.isVisible) {
                    _binding?.clSFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunsethd) }
                }
            }
            (currentTimeFormat.toInt()>=12) and (currentTimeFormat.toInt()<16) -> {
                if (DashBoard.settingsFragment.isVisible) {
                    _binding?.clSFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.noonhd) }
                }
            }
            else -> {
                if (DashBoard.settingsFragment.isVisible) {
                    _binding?.clSFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.nighthd) }
                }
            }
        }
    }

    private fun toggleButtonCheckListener() {
        _binding?.toggleButton?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                DashBoard.units="imperial"
                (activity as DashBoard?)?.getLocation()
            }else{
                DashBoard.units="metric"
                (activity as DashBoard?)?.getLocation()
            }
        }
    }
}