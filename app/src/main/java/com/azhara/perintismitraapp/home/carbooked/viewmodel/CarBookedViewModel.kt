package com.azhara.perintismitraapp.home.carbooked.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintismitraapp.entity.CarBooked
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CarBookedViewModel : ViewModel(){

    private val TAG = CarBookedViewModel::class.java.simpleName
    private val db = FirebaseFirestore.getInstance()
    private val partnerId = FirebaseAuth.getInstance().currentUser?.uid
    private val dataBooked = MutableLiveData<ArrayList<CarBooked>>()
    val data = ArrayList<CarBooked>()

    fun getDataCarBooked(){
        val dbDataBooked = partnerId?.let { db.collection("partners").document(it).collection("bookingCar") }
        dbDataBooked?.get()?.addOnSuccessListener { documents ->
            if (documents != null) {
                for (doc in documents){
                    val time = doc.getTimestamp("startDate")
                    val statusBooking = doc.getLong("statusBooking")
                    val duration = doc.getLong("duration")
                    val userId = doc.getString("userId")
                    val carId = doc.getString("carId")
                    val totalPrice = doc.getLong("totalPrice").toString()

                    if (userId != null) {
                        getDataUser(userId, time, statusBooking, duration, carId, totalPrice)
                    }
                }
            }
        }
    }

    private fun getDataUser(
        userId: String?,
        time: Timestamp?,
        statusBooking: Long?,
        duration: Long?,
        carId: String?,
        totalPrice: String
    ){
        val userDb = userId?.let { db.collection("users").document(it) }
        userDb?.addSnapshotListener { value, error ->
            if (error != null){
                Log.e(TAG, "getDataUser ${error.message}")
                return@addSnapshotListener
            }
            if (value != null && value.exists()){
                val userName = value.getString("name")
                getDataCar(carId, userName, time, statusBooking, duration, totalPrice, userId)
            }
        }
    }

    private fun getDataCar(
        id: String?,
        userName: String?,
        time: Timestamp?,
        statusBooking: Long?,
        duration: Long?,
        totalPrice: String,
        userId: String
    ){
        val carDb = id?.let { db.collection("cars").document(it) }
        val booked = CarBooked()
        carDb?.addSnapshotListener { value, error ->
            if (error != null){
                Log.e(TAG, "getDataCar ${error.message}")
                return@addSnapshotListener
            }
            if (value != null && value.exists()){
                val carName = value.getString("carName")
                booked.userName = userName
                booked.carName = carName
                booked.time = time
                booked.statusBooking = statusBooking
                booked.duration = duration
                booked.totalPrice = totalPrice
                booked.carId = id
                booked.userId = userId

                data.add(booked)
                dataBooked.postValue(data)
            }
        }
    }

    fun dataBooked(): LiveData<ArrayList<CarBooked>> = dataBooked
}