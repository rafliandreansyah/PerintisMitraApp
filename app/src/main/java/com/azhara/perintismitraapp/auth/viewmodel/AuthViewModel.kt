package com.azhara.perintismitraapp.auth.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintismitraapp.entity.Partner
import com.azhara.perintismitraapp.entity.PartnerRegister
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class AuthViewModel : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val statusLogin = MutableLiveData<Boolean>()
    private val statusResetPassword = MutableLiveData<Boolean>()
    private val statusRegisterPartner = MutableLiveData<Boolean>()
    private val listDataTypeCar = MutableLiveData<ArrayList<String>>()
    var messageError: String? = null

    private fun checkMitra(userId: String?){
        val mitraDb = userId?.let { db.collection("partners").document(it) }
        mitraDb?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()){
                val partner = documentSnapshot.toObject(Partner::class.java)
                if (partner?.statusActive == false){
                    statusLogin.postValue(false)
                    messageError = "Akun anda terblokir, silahkan hubungi admin untuk bantuan!"
                }else{
                    statusLogin.postValue(true)
                }
            }else{
                statusLogin.postValue(false)
                messageError = "Akun anda tidak terdaftar mitra perintis!"
            }
        }?.addOnFailureListener { exception ->
            statusLogin.postValue(false)
            messageError = exception.message
        }
    }

    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val userId = auth.currentUser?.uid
               checkMitra(userId)
            }else{
                statusLogin.postValue(false)
                messageError = task.exception?.message.toString()
                Log.e("AuthViewModel", task.exception?.message.toString())
            }
        }
    }

    fun statusLogin() : LiveData<Boolean> = statusLogin

    fun resetPassword(email: String){
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful){
                statusResetPassword.postValue(true)
            }else{
                statusResetPassword.postValue(false)
            }
        }
    }

    fun statusResetPassword(): LiveData<Boolean> = statusResetPassword

    fun getCarType(){
        val carTypeDb = db.collection("list_car_type")

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

    fun uploadDataMitra(
        imgByteArray: ByteArray,
        name: String?,
        email: String?,
        phone: String?,
        address: String,
        typeCar: String,
        transmisionCar: String,
        carYear: String,
        numberRegister: String){

        val imgStorage = email?.let { storage.reference.child("image_mitra_register").child(it) }

        val storageTask = imgStorage?.putBytes(imgByteArray)
        storageTask?.addOnFailureListener{

        }?.addOnSuccessListener {
            storageTask.continueWithTask { task ->
                if (!task.isSuccessful){

                }
                imgStorage.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUri = task.result
                    uploadDataMitraFireStore(imgUri.toString(), name, email, phone, address, typeCar, transmisionCar, carYear, numberRegister)
                }else{
                    
                }
            }
        }
    }

    private fun uploadDataMitraFireStore(img: String?,
                                         name: String?,
                                         email: String?,
                                         phone: String?,
                                         address: String,
                                         typeCar: String,
                                         transmisionCar: String,
                                         carYear: String,
                                         numberRegister: String){
        var transmision = 0
        if (transmisionCar == "Automatic"){
            transmision = 1
        }

        val dataRegisterPartner = PartnerRegister(email, name, address, phone?.toLong(), typeCar, carYear.toInt(), numberRegister, transmision, img)
        val registerDb = db.collection("partner_register").add(dataRegisterPartner)
        registerDb.addOnCompleteListener {
            if (it.isSuccessful){
                statusRegisterPartner.postValue(true)
            }else{
                Log.e("Error Register", it.exception?.message)
                statusRegisterPartner.postValue(true)
            }
        }
    }

    fun checkRegister(): LiveData<Boolean> = statusRegisterPartner

}