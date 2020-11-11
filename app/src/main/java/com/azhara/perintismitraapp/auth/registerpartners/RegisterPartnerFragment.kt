package com.azhara.perintismitraapp.auth.registerpartners

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.auth.viewmodel.AuthViewModel
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_register_partner.*
import java.io.ByteArrayOutputStream

class RegisterPartnerFragment : Fragment(), View.OnClickListener {

    private lateinit var authViewModel: AuthViewModel
    private var requestImg = 1
    private var imgUri: Uri? = null
    private var bitmapImage: Bitmap? = null

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
        btn_add_image_register_car.setOnClickListener(this)

        authViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AuthViewModel::class.java]
        authViewModel.getCarType()
        getDataCarTypeAndTransmision()
        checkRegister()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSendRegisterMitra -> {
                checkForm()
            }
            R.id.btn_add_image_register_car -> {
                openGallery()
            }
        }
    }

    private fun getDataCarTypeAndTransmision(){
        authViewModel.dataCarType().observe(viewLifecycleOwner, Observer { data ->
            val adapter = ArrayAdapter(requireContext(), R.layout.item_list_car_type, data)
            car_type.setAdapter(adapter)
        })

        val itemTransmission = listOf<String>("Manual", "Automatic")
        val transmisionAdapter = ArrayAdapter(requireContext(), R.layout.item_list_car_transmisi, itemTransmission)
        car_transmision.setAdapter(transmisionAdapter)
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestImg)
    }

    private fun imageByteArray(bitmap: Bitmap?): ByteArray {
        val bitmapCompress = bitmap?.let { resizeBitmap(it, 700) } //resize bitmap file
        val baos = ByteArrayOutputStream()
        bitmapCompress?.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        ) //compress bitmap extension to JPEG

        return baos.toByteArray()
    }

    // resize filebitmap with specific size
    private fun resizeBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width //get width image
        var height = image.height //get heigh image

        val bitMapRatio = width.toFloat() / height.toFloat()
        if (bitMapRatio > 1) {
            width = maxSize
            height = (width / bitMapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitMapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    // update data and edittext handler
    private fun checkForm() {
        val name = edt_name_register.text.toString().trim()
        val address = edt_address_register.text.toString().trim()
        val email = edt_email_register.text.toString().trim()
        val phone = edt_phone_register.text.toString().trim()
        val typeCar = car_type.text.toString().trim()
        val transmisionCar = car_transmision.text.toString().trim()
        val carYear = edt_year_register.text.toString().trim()
        val numberRegister = edt_number_car_register.text.toString().trim()

        if (name.isEmpty()) {
            layout_edt_name_register.error = "Nama tidak boleh kosong!"
            return
        }
        if (address.isEmpty()) {
            layout_edt_address_register.error = "Alamat tidak boleh kosong!"
            return
        }
        if (email.isEmpty()) {
            layout_edt_email_register.error = "Email tidak boleh kosong!"
            return
        }
        if (phone.isEmpty()) {
            layout_edt_email_register.error = "Phone tidak boleh kosong!"
            return
        }
        if (typeCar.isEmpty()) {
            layout_edt_email_register.error = "Tipe mobil tidak boleh kosong!"
            return
        }
        if (transmisionCar.isEmpty()) {
            layout_edt_email_register.error = "Transmisi mobil tidak boleh kosong!"
            return
        }
        if (carYear.isEmpty()) {
            layout_edt_email_register.error = "Tahun mobil tidak boleh kosong!"
            return
        }
        if (numberRegister.isEmpty()) {
            layout_edt_email_register.error = "Nomer registrasi mobil tidak boleh kosong!"
            return
        }

        if (!checkbox_agree.isChecked){
            checkbox_agree.error = "Anda belum menyetujui persyaratan!"
            return
        }

        if (imgUri == null){
            activity?.let { Toasty.error(it, "Pilih gambar mobil terlebih dahulu", Toast.LENGTH_SHORT, true).show() }
            return
        }

        if (checkbox_agree.isChecked && name.isNotEmpty() && address.isNotEmpty()
            && email.isNotEmpty() && phone.isNotEmpty() && typeCar.isNotEmpty()
            && transmisionCar.isNotEmpty() && carYear.isNotEmpty() && numberRegister.isNotEmpty()
            && imgUri != null){
            authViewModel.uploadDataMitra(imageByteArray(bitmapImage), name, email, phone, address, typeCar, transmisionCar, carYear, numberRegister)
        }

    }

    private fun checkRegister(){
        authViewModel.checkRegister().observe(viewLifecycleOwner, Observer { status ->
            if (status){
                view?.findNavController()?.navigate(R.id.action_registerPartnerFragment_to_registerPartnerInfoFragment)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == requestImg && data != null && data.data != null) {
            imgUri = data.data
            activity?.let { Glide.with(it).load(imgUri).into(img_car_mitra_register) }

            if (imgUri != null) {
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(activity?.contentResolver, imgUri)
                        Log.d("EditProfileFragment", "bitmap android sdk < 28 $bitmap")
                        bitmapImage = bitmap

                    } else {
                        val source =
                            activity?.contentResolver?.let {
                                ImageDecoder.createSource(
                                    it,
                                    imgUri!!
                                )
                            }
                        val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                        Log.d("EditProfileFragment", "bitmap android sdk 28 $bitmap")
                        bitmapImage = bitmap
                    }
                } catch (e: Exception) {
                    Log.e("EditProfileFragment", "${e.message}")
                }
            }
        }
    }

}