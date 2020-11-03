package com.azhara.perintismitraapp.auth.resetpassword

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
import com.azhara.perintismitraapp.auth.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AuthViewModel::class.java]

        btn_reset_password.setOnClickListener {
            authViewModel.resetPassword(edt_reset_password.text.toString().trim())
        }

        checkBtnActive()
        statusResetPassword()
    }

    private fun checkBtnActive(){

        val watcher = object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btn_reset_password.isEnabled = s.length >= 5
            }

        }
        edt_reset_password.addTextChangedListener(watcher)
    }

    private fun statusResetPassword(){
        authViewModel.statusResetPassword().observe(viewLifecycleOwner, Observer { status ->
            if (status == true){
                view?.findNavController()?.navigate(R.id.action_resetPasswordFragment_to_resetPasswordInfoFragment)
            }else{
                Toast.makeText(context, "Reset password gagal!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}