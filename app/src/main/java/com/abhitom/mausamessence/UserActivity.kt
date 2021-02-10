package com.abhitom.mausamessence

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abhitom.mausamessence.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var prefs:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        prefs=this.getSharedPreferences(
            getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )

        btnUANextOnClickListener()
    }

    private fun btnUANextOnClickListener() {
        binding.btnUANext.setOnClickListener {
            val name=binding.etUAName.text.toString()
            if (name.isBlank() || name.isEmpty()){
                Toast.makeText(this,"Invalid UserName", Toast.LENGTH_LONG).show()
            }else {
                prefs.edit().putString("Name", name).apply()
                val intent = Intent(this,DashBoard::class.java)
                intent.clearStack()
                startActivity(intent)
            }
        }
    }

    private fun Intent.clearStack(additionalFlags: Int = 0) {
        flags = additionalFlags or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
}