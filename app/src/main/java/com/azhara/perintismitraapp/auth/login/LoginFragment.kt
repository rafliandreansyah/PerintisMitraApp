package com.azhara.perintismitraapp.auth.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.auth.viewmodel.AuthViewModel
import com.azhara.perintismitraapp.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), View.OnClickListener {

    private var twicePressedBack = false

    private lateinit var toast: Toast
    private lateinit var authViewModel: AuthViewModel

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            startActivity(Intent(context, HomeActivity::class.java))
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener(this)
        btnResetPassword.setOnClickListener(this)
        btnToRegister.setOnClickListener(this)

        authViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AuthViewModel::class.java]

        mitraStatusLogin()
        backPressed()
        checkButtonActive()
    }

    private fun checkPartner(){
        authViewModel.login(edt_email.text.toString().trim(), edt_password.text.toString().trim())
    }

    private fun mitraStatusLogin(){
        authViewModel.statusLogin().observe(viewLifecycleOwner, Observer {
            if (it){
                startActivity(Intent(context, HomeActivity::class.java))
            }else{
                Toast.makeText(context, authViewModel.messageError, Toast.LENGTH_SHORT).show()
                authViewModel.messageError?.let { it1 -> errorMessage(it1) }
            }

        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin ->{
//                startActivity(Intent(context, HomeActivity::class.java))
                checkPartner()
            }
            R.id.btnResetPassword->{
                 view?.findNavController()?.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }
            R.id.btnToRegister->{
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_registerPartnerFragment)
            }
        }
    }

    private fun backPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (twicePressedBack){
                    toast.cancel()
                    activity?.moveTaskToBack(true)
                    activity?.finish()
                }else{
                    twicePressedBack = true
                    toast = Toast.makeText(context, "Press back again to exit!", Toast.LENGTH_SHORT)
                    toast.show()
                }

                Handler().postDelayed(Runnable { twicePressedBack = false }, 2000)
            }

        })
    }

    private fun checkButtonActive(){
        val watcher = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                layout_edt_login_password.error = null
                layout_edt_login_email.error = null
                if (s?.length == 0){
                    btnLogin.isEnabled = false
                }else{
                    btnLogin.isEnabled =
                        !(edt_email.text.toString() == "" || edt_password.text.toString() == "")
                }
            }
        }
        edt_email.addTextChangedListener(watcher)
        edt_password.addTextChangedListener(watcher)
    }

    private fun errorMessage(errorMessage: String){
        if (errorMessage == getString(R.string.error_message_password_from_firebase)){
            layout_edt_login_password.error = getString(R.string.error_message_password)
        }else if (errorMessage == getString(R.string.error_message_email_not_register)){
            layout_edt_login_email.error = getString(R.string.error_message_email_not_register)
        }
    }

}