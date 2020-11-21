package com.azhara.perintismitraapp.home.car.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintismitraapp.entity.CarData
import com.azhara.perintismitraapp.home.car.adapter.ListCarAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CarViewModel : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = auth.currentUser?.email
    private val dataCar = MutableLiveData<ArrayList<CarData>>()

    fun getDataCar(){
        val carDb = db.collection("cars").whereEqualTo("partnerId", userEmail)
        carDb.addSnapshotListener { value, error ->
            if (error != null){

            }
            if (value != null){
                Log.d("Car", "${value.toObjects(CarData::class.java)}")
                val data = value.toObjects(CarData::class.java)
                dataCar.postValue(data as ArrayList<CarData>?)
            }
        }
    }

    fun dataCar(): LiveData<ArrayList<CarData>> = dataCar

}