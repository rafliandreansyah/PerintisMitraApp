package com.azhara.perintismitraapp.auth.registerpartners

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.auth.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register_partner.*

class RegisterPartnerFragment : Fragment(), View.OnClickListener {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_partner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSendRegisterMitra.setOnClickListener(this)

        authViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AuthViewModel::class.java]
        authViewModel.getCarType()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSendRegisterMitra -> {
                view?.findNavController()?.navigate(R.id.action_registerPartnerFragment_to_registerPartnerInfoFragment)
            }
        }
    }

}