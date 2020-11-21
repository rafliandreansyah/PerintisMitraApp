package com.azhara.perintismitraapp.home.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.azhara.perintismitraapp.R
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_setting.setOnClickListener(this)
        btn_list_car.setOnClickListener(this)
        btn_car_booked.setOnClickListener(this)
        btn_register_car.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_setting -> {

            }
            R.id.btn_list_car -> {
                view?.findNavController()?.navigate(R.id.action_dashboardFragment_to_carFragment)
            }
            R.id.btn_car_booked -> {
                view?.findNavController()?.navigate(R.id.action_dashboardFragment_to_carBookedFragment)
            }
            R.id.btn_register_car -> {

            }
        }
    }

}