package com.azhara.perintismitraapp.home.profile.editprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.home.profile.viewmodel.EditProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EdtiProfileFragment : Fragment() {

    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editProfileViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EditProfileViewModel::class.java]
        editProfileViewModel.getDataMitra()

        checkTextWatcher()
        dataMitra()
    }

    private fun checkTextWatcher(){
        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    btn_save_edt_profile.isEnabled = false
                }else{
                    btn_save_edt_profile.isEnabled = edt_address.text.toString().trim() != "" && edt_name.text.toString().trim() != "" && edt_phone.text.toString().trim() != ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        edt_name.addTextChangedListener(watcher)
        edt_phone.addTextChangedListener(watcher)
        edt_address.addTextChangedListener(watcher)
    }

    private fun dataMitra(){
        editProfileViewModel.dataMitra().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                setData(data.imgUrl, data.owner, data.phone.toString(), data.address)
            }
        })
    }

    private fun setData(imgUrl: String?, name: String?, phoneNumber: String?, address: String?){
        if (imgUrl != null || imgUrl != ""){
            context?.let { Glide.with(it).load(imgUrl).into(img_profile) }
        }
        edt_name.setText(name)
        edt_address.setText(address)
        edt_phone.setText(phoneNumber)
    }

}