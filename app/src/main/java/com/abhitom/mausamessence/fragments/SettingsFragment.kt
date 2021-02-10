package com.abhitom.mausamessence.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    lateinit var prefs:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        if (DashBoard.settingsFragment.isVisible) {
            prefs = context!!.getSharedPreferences(
                getString(R.string.parent_package_name),
                Context.MODE_PRIVATE
            )
        }
        _binding?.txtUsername!!.text="Hi, "+DashBoard.userName
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