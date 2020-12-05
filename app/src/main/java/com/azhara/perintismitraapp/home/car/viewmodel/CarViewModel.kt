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
                val cars = ArrayList<CarData>()
                val doc = value.documents
                doc.forEach {
                    val carUpdate = it.toObject(CarData::class.java)
                    if (carUpdate != null){
                        carUpdate.carId = it.id
                        cars.add(carUpdate)
                    }
                }
                Log.d("Car", "$cars")
                dataCar.postValue(cars)
            }
        }
    }

    fun dataCar(): LiveData<ArrayList<CarData>> = dataCar

    fun editCarEnable(isReady: Boolean, carId: String){
        val carData = db.collection("cars").document(carId)
        carData.update("statusReady", isReady).addOnSuccessListener {
            if (it != null){
                Log.d("CarViewModel", it.toString())
            }
        }.addOnFailureListener {
            Log.e("CarViewModel", it.message.toString())
        }

    }

}