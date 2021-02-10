package com.abhitom.mausamessence.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

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
        _binding?.switchSFTemp?.setOnCheckedChangeListener { buttonView, isChecked ->
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