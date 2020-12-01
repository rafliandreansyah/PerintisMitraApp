package com.azhara.perintismitraapp.home.profile.changepassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.home.profile.viewmodel.EditProfileViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : Fragment() {

    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editProfileViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EditProfileViewModel::class.java]

        checkBtnActive()
        btnChangePassword.setOnClickListener {
            checkPassword()
        }
        checkReAuth()
    }

    private fun checkBtnActive(){
        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    btnChangePassword.isEnabled = false
                }else{
                    btnChangePassword.isEnabled = !(edt_old_password.text.toString().trim() == "" && edt_new_password.text.toString().trim() == "" && edt_confirm_new_password.text.toString().trim() == "")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        edt_old_password.addTextChangedListener(watcher)
        edt_new_password.addTextChangedListener(watcher)
        edt_confirm_new_password.addTextChangedListener(watcher)
    }

    private fun checkPassword(){
        val oldPassword = edt_old_password.text.toString().trim()
        val newPassword = edt_new_password.text.toString().trim()
        val confirmPassword = edt_confirm_new_password.text.toString().trim()

        if (oldPassword == newPassword){
            layout_edt_new_password.error = "Password baru tidak boleh sama dengan password lama."
            return
        }
        if (newPassword != confirmPassword){
            layout_edt_confirm_new_password.error = "Password tidak sama."
        }

        editProfileViewModel.reAuth(oldPassword, newPassword)
    }

    private fun checkReAuth(){
        editProfileViewModel.checkReAuth().observe(viewLifecycleOwner, Observer { status ->
            if (status){
                checkChangePassword()
            }else{
                context?.let { Toasty.error(it, "Password lama salah", Toast.LENGTH_SHORT, true).show() }
            }
        })
    }

    private fun checkChangePassword(){
        editProfileViewModel.changePasswordStatus().observe(viewLifecycleOwner, Observer { status ->
            if (status){
                view?.findNavController()?.navigate(R.id.action_navigation_change_password_to_navigation_profile)
                context?.let { Toasty.success(it, "Ganti password berhasil", Toast.LENGTH_SHORT, true).show() }
            }else{
                context?.let { Toasty.error(it, "Ganti password gagal", Toast.LENGTH_SHORT, true).show() }
            }
        })
    }

}