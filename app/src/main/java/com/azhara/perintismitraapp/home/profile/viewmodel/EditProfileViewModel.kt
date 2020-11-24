package com.azhara.perintismitraapp.home.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintismitraapp.entity.Partner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class EditProfileViewModel : ViewModel(){

    private val mitraEmail = FirebaseAuth.getInstance().currentUser?.email
    private val db = FirebaseFirestore.getInstance()
    private val TAG = EditProfileViewModel::class.java.simpleName
    private val dataMitra =  MutableLiveData<Partner>()
    private val updateDataStatus = MutableLiveData<Boolean>()

    fun getDataMitra(){
        val mitraDb = mitraEmail?.let { db.collection("partners").document(it) }

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

    fun updateData(imgUrl: String?, name: String?, phone: Long?, address: String?){
        val dbPartner = mitraEmail?.let { db.collection("partners").document(it) }

        val partner = HashMap<String, Any?>()
        partner["name"] = name
        partner["phone"] = phone
        partner["address"] = address
        if (imgUrl != null || imgUrl != ""){
            partner["imgUrl"] = imgUrl
        }

        dbPartner?.update(partner)?.addOnSuccessListener {
            updateDataStatus.postValue(true)
        }?.addOnFailureListener { e ->
            updateDataStatus.postValue(false)
            Log.e(TAG, "Error update data => ${e.message}")
        }
    }

    fun updateDataStatus(): LiveData<Boolean> = updateDataStatus

}