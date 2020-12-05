package com.azhara.perintismitraapp.home.car

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.entity.CarData
import com.azhara.perintismitraapp.home.car.adapter.ListCarAdapter
import com.azhara.perintismitraapp.home.car.viewmodel.CarViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_car.*

class CarFragment : Fragment() {

    private lateinit var listCarAdapter: ListCarAdapter
    private lateinit var carViewModel: CarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CarViewModel::class.java]
        listCarAdapter = ListCarAdapter()

        val layoutManager = LinearLayoutManager(context)
        rv_car.setHasFixedSize(true)
        rv_car.layoutManager = layoutManager

        carViewModel.getDataCar()
        setData()
        onItemClicked()
    }

    private fun setData(){
        carViewModel.dataCar().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                listCarAdapter.submitList(data)
                rv_car.adapter = listCarAdapter
            }
        })
    }

    private fun onItemClicked(){
        listCarAdapter.setOnItemClicked(object : ListCarAdapter.ItemClicked{
            override fun onItemClicked(data: CarData) {
                dialog(data)
            }

        })
    }

    private fun dialog(carData: CarData){
        val dialog = context?.let { MaterialAlertDialogBuilder(it) }
        if (carData.statusReady == true){
            dialog?.setTitle("Non Aktifkan Mobil")
                ?.setMessage("Apakah anda yakin mau menonaktifkan mobil ${carData.carName}?")
                ?.setNegativeButton("Tidak"){ _, _ ->
                }
                ?.setPositiveButton("Iya"){dialog, which ->
                    carData.carId?.let { editCarActive(false, it) }
                }
        }else{
            dialog?.setTitle("Aktifkan Mobil")
                ?.setMessage("Apakah anda yakin mau mengaktifkan mobil ${carData.carName}?")
                ?.setNegativeButton("Tidak"){ _, _ ->
                }
                ?.setPositiveButton("Iya"){_, _ ->
                    carData.carId?.let { editCarActive(true, it) }
                }
        }
        dialog?.show()
    }

    private fun editCarActive(isActive: Boolean, carId: String){
        carViewModel.editCarEnable(isActive, carId)
    }
}