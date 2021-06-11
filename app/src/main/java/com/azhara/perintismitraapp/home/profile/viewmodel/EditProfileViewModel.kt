package com.azhara.perintismitraapp.home.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintismitraapp.entity.Partner
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class EditProfileViewModel : ViewModel(){

    private val mitraId = FirebaseAuth.getInstance().currentUser?.uid
    private val mitraEmail = FirebaseAuth.getInstance().currentUser?.email
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val TAG = EditProfileViewModel::class.java.simpleName
    private val dataMitra =  MutableLiveData<Partner>()
    private val updateDataStatus = MutableLiveData<Boolean>()
    private val updatePassword = MutableLiveData<Boolean>()
    private val checkReAuth = MutableLiveData<Boolean>()

    fun getDataMitra(){
        val mitraDb = mitraId?.let { db.collection("partners").document(it) }

        mitraDb?.addSnapshotListener { value, error ->
            if (error != null){
                Log.e(TAG, "Error getDataMitra => ${error.message}")
            }
            if (value != null && value.exists()){
                val data = value.toObject(Partner::class.java)
                dataMitra.postValue(data)
            }
        }
    }

    fun dataMitra(): LiveData<Partner> = dataMitra

    fun updateData(name: String?, phone: Long?, address: String?){
        val dbPartner = mitraId?.let { db.collection("partners").document(it) }

        val partner = HashMap<String, Any?>()
        partner["name"] = name
        partner["phone"] = phone
        partner["address"] = address

        dbPartner?.update(partner)?.addOnSuccessListener {
            updateDataStatus.postValue(true)
        }?.addOnFailureListener { e ->
            updateDataStatus.postValue(false)
            Log.e(TAG, "Error update data => ${e.message}")
        }
    }

    fun updateDataStatus(): LiveData<Boolean> = updateDataStatus

    fun reAuth(oldPassword: String?, newPassword: String?){
        val partner = auth.currentUser
        val credential = mitraEmail?.let { oldPassword?.let { it1 ->
            EmailAuthProvider.getCredential(it,
                it1
            )
        } }

        credential?.let { partner?.reauthenticate(it)?.addOnCompleteListener { authenticated ->
            if (authenticated.isSuccessful){
                changePassword(newPassword)
                checkReAuth.postValue(true)
            }else{
                checkReAuth.postValue(false)
                Log.e(TAG, "ReAuth Failed => ${authenticated.exception?.message}")
            }
        } }
    }

    fun checkReAuth(): LiveData<Boolean> = checkReAuth

    private fun changePassword(newPassword: String?){
        val partner = auth.currentUser

        newPassword?.let {
            partner?.updatePassword(it)?.addOnCompleteListener { update ->
                if (update.isSuccessful){
                    updatePassword.postValue(true)
                }else{
                    Log.e(TAG, "Error update => ${update.exception?.message}")
                    updatePassword.postValue(true)
                }
            }
        }
    }

    fun changePasswordStatus(): LiveData<Boolean> = updatePassword

}