package com.azhara.perintismitraapp.home.carbooked

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.home.carbooked.adapter.CarBookedAdapter
import com.azhara.perintismitraapp.home.carbooked.viewmodel.CarBookedViewModel
import kotlinx.android.synthetic.main.fragment_car_booked.*

class CarBookedFragment : Fragment() {

    private lateinit var carBookedViewModel: CarBookedViewModel
    private lateinit var carBookedAdapter: CarBookedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_booked, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carBookedAdapter = CarBookedAdapter()
        carBookedViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CarBookedViewModel::class.java]
        carBookedViewModel.getDataCarBooked()

        rv_car_booked.layoutManager = LinearLayoutManager(activity)
        rv_car_booked.setHasFixedSize(true)
        dataCar()
    }

    private fun dataCar(){
        carBookedViewModel.dataBooked().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                carBookedAdapter.submitList(data)
                rv_car_booked.adapter = carBookedAdapter
            }
        })
    }
}