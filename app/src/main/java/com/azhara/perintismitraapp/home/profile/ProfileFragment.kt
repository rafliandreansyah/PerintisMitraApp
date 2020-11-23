package com.azhara.perintismitraapp.home.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.auth.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_edit_profile.setOnClickListener(this)
        btn_change_password.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_edit_profile -> view?.findNavController()?.navigate(R.id.action_profileFragment_to_editProfileFragment)
            R.id.btn_change_password -> view?.findNavController()?.navigate(R.id.action_profileFragment_to_changePasswordFragment)
            R.id.btn_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            }
        }
    }


}