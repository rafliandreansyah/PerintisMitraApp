package com.azhara.perintismitraapp.home.carregister.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintismitraapp.entity.CarRegister
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CarRegisterViewModel : ViewModel(){

    private val registerCarDb = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val listDataTypeCar = MutableLiveData<ArrayList<String>>()
    private val statusRegisterCar = MutableLiveData<Boolean>()

    fun getCarType(){
        val carTypeDb = registerCarDb.collection("list_car_type")

        carTypeDb.get().addOnCompleteListener { result ->
            val dataCar = ArrayList<String>()
            for (query in result.result!!){
                dataCar.add(query["carType"].toString())
                Log.d("query", query["carType"].toString())
            }
            listDataTypeCar.postValue(dataCar)
        }.addOnFailureListener { exception ->
            Log.e("Get Car Type", exception.message)
        }
    }

    fun dataCarType(): LiveData<ArrayList<String>> = listDataTypeCar

    fun uploadCarMitra(
        imgByteArray: ByteArray,
        typeCar: String,
        transmisionCar: String,
        carYear: String,
        numberRegister: String){

        val imgStorage =
            auth.currentUser?.email?.let { storage.reference.child("image_car_register").child(it) }

        val storageTask = imgStorage?.putBytes(imgByteArray)
        storageTask?.addOnFailureListener{

        }?.addOnSuccessListener {
            storageTask.continueWithTask { task ->
                if (!task.isSuccessful){
                    Log.e("CarRegister", task.exception?.message.toString())
                }
                imgStorage.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUri = task.result
                    uploadCarMitraFireStore(imgUri.toString(), typeCar, transmisionCar, carYear, numberRegister)
                }else{
                    Log.e("CarRegister", task.exception?.message.toString())
                }
            }
        }
    }

    private fun uploadCarMitraFireStore(
        img: String,
        typeCar: String,
        transmisionCar: String,
        carYear: String,
        numberRegister: String
    ) {
        val dataCar = CarRegister(auth.currentUser?.email, typeCar, transmisionCar, carYear.toLong(), numberRegister, img)
        val db = registerCarDb.collection("car_register").add(dataCar)
        db.addOnSuccessListener {
            statusRegisterCar.postValue(true)
        }.addOnFailureListener {
            statusRegisterCar.postValue(false)
        }

    }

    fun checkRegisterCar(): LiveData<Boolean> = statusRegisterCar

}