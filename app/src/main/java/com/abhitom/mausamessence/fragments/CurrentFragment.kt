package com.abhitom.mausamessence.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.abhitom.mausamessence.DashBoard
import com.abhitom.mausamessence.R
import com.abhitom.mausamessence.databinding.FragmentCurrentBinding
import java.text.SimpleDateFormat
import java.util.*


class CurrentFragment : Fragment() {

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
    }

    // function to change background automatically wrt time
    private fun changeBackground() {
        val currentTime: Long = System.currentTimeMillis()
        val currentTimeDate = Date(currentTime)
        val currentTimeFormat = SimpleDateFormat("HH").format(currentTimeDate)

        when {
            (currentTimeFormat.toInt()>6) and (currentTimeFormat.toInt()<12) -> {
                if (DashBoard.currentFragment.isVisible) {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunrisehd) }
                }
            }
            (currentTimeFormat.toInt()>=16) and (currentTimeFormat.toInt()<21) -> {
                if (DashBoard.currentFragment.isVisible) {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.sunsethd) }
                }
            }
            (currentTimeFormat.toInt()>=12) and (currentTimeFormat.toInt()<16) -> {
                if (DashBoard.currentFragment.isVisible) {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.noonhd) }
                }
            }
            else -> {
                if (DashBoard.currentFragment.isVisible) {
                    _binding?.clCFLayout?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.nighthd) }
                }
            }
        }
    }

}