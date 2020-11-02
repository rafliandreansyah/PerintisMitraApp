package com.azhara.perintismitraapp.auth.registerpartners

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.azhara.perintismitraapp.R
import kotlinx.android.synthetic.main.fragment_register_partner_info.*

class RegisterPartnerInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_partner_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnToLogin.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerPartnerInfoFragment_to_loginFragment)
        }

    }

}