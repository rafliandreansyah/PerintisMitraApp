package com.azhara.perintismitraapp.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.azhara.perintismitraapp.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navController = findNavController(R.id.nav_host_home_fragment)

        val appBarConfiguration =  AppBarConfiguration(
            setOf(
                R.layout.fragment_dashboard,
                R.layout.fragment_car,
                R.layout.fragment_car_booked,
                R.layout.fragment_car_register,
                R.layout.fragment_edit_profile
            )
        )

        toolbar_home.setupWithNavController(navController, appBarConfiguration)
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)
    }
}