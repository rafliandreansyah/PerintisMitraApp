package com.azhara.perintismitraapp.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintismitraapp.entity.Partner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val statusLogin = MutableLiveData<Boolean>()
    var messageError: String? = null

    fun checkMitra(email: String, password: String){
        val mitraDb = db.collection("partners").document(email)
        mitraDb.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()){
                val partner = documentSnapshot.toObject(Partner::class.java)
                if (partner?.statusActive == false){
                    statusLogin.postValue(false)
                    messageError = "Akun anda terblokir, silahkan hubungi admin untuk bantuan!"
                }else{
                    login(email, password)
                }
            }else{
                statusLogin.postValue(false)
                messageError = "Akun anda tidak terdaftar mitra perintis!"
            }
        }.addOnFailureListener { exception ->
            statusLogin.postValue(false)
            messageError = exception.message
        }
    }

    private fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                statusLogin.postValue(true)
            }else{
                statusLogin.postValue(false)
                messageError = task.exception?.message.toString()
                Log.e("AuthViewModel", task.exception?.message.toString())
            }
        }
    }

    fun statusLogin() : LiveData<Boolean> = statusLogin

}