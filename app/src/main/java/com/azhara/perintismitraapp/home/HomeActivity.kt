package com.azhara.perintismitraapp.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.azhara.perintismitraapp.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_home_fragment) as NavHostFragment
        val navController = navHost.navController

        val appBarConfiguration =  AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_profile,
                R.id.navigation_car,
                R.id.navigation_car_register,
                R.id.navigation_edit_profile,
                R.id.navigation_change_password,
                R.id.navigation_car_booked
            )
        )

        toolbar_home.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_dashboard -> {
                    toolbar_home.visibility = View.GONE
                }
                R.id.navigation_profile -> {
                    toolbar_home.visibility = View.VISIBLE
                    toolbar_home.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
                    setSupportActionBar(toolbar_home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.navigation_car -> {
                    toolbar_home.visibility = View.VISIBLE
                    toolbar_home.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
                    setSupportActionBar(toolbar_home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.navigation_car_register -> {
                    toolbar_home.visibility = View.VISIBLE
                    toolbar_home.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
                    setSupportActionBar(toolbar_home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.navigation_edit_profile -> {
                    toolbar_home.visibility = View.VISIBLE
                    toolbar_home.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
                    setSupportActionBar(toolbar_home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.navigation_change_password -> {
                    toolbar_home.visibility = View.VISIBLE
                    toolbar_home.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
                    setSupportActionBar(toolbar_home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.navigation_car_booked -> {
                    toolbar_home.visibility = View.VISIBLE
                    toolbar_home.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
                    setSupportActionBar(toolbar_home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}