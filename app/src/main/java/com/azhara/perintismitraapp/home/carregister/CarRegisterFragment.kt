package com.azhara.perintismitraapp.home.carregister

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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
import com.azhara.perintismitraapp.home.carregister.viewmodel.CarRegisterViewModel
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_car_register.*
import kotlinx.android.synthetic.main.fragment_car_register.btn_add_image_register_car
import kotlinx.android.synthetic.main.fragment_register_partner.*
import java.io.ByteArrayOutputStream

class CarRegisterFragment : Fragment(), View.OnClickListener {

    private var requestImg = 1
    private var imgUri: Uri? = null
    private var bitmapImage: Bitmap? = null
    private lateinit var carRegisterViewModel: CarRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carRegisterViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CarRegisterViewModel::class.java]
        carRegisterViewModel.getCarType()
        getDataCarTypeAndTransmision()
        checkEditText()
        checkUpload()
        btn_add_image_register_car.setOnClickListener(this)
        btnSendRegisterCarMitra.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add_image_register_car -> openGallery()
            R.id.btnSendRegisterCarMitra -> uploadRegister()
        }
    }

    private fun getDataCarTypeAndTransmision(){
        carRegisterViewModel.dataCarType().observe(viewLifecycleOwner, Observer { data ->
            val adapter = ArrayAdapter(requireContext(), R.layout.item_list_car_type, data)
            edt_car_type_register_car.setAdapter(adapter)
        })

        val itemTransmission = listOf("Manual", "Automatic")
        val transmisionAdapter = ArrayAdapter(requireContext(), R.layout.item_list_car_transmisi, itemTransmission)
        edt_transmission_register_car.setAdapter(transmisionAdapter)
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

    private fun checkEditText(){

        val edtCarType = edt_car_type_register_car.text.toString().trim()
        val edtTransmission = edt_transmission_register_car.text.toString().trim()
        val carYear = edt_year_register_car.text.toString().trim()
        val edtNumberCar = edt_number_register_car.text.toString().trim()

        val watcher = object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    btnSendRegisterCarMitra.isEnabled = false
                }else{
                    btnSendRegisterCarMitra.isEnabled = (edt_car_type_register_car.text.toString().trim() != "" && edt_transmission_register_car.text.toString().trim() != ""
                            && edt_year_register_car.text.toString().trim() != "" && edt_number_register_car.text.toString().trim() != "")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        edt_car_type_register_car.addTextChangedListener(watcher)
        edt_transmission_register_car.addTextChangedListener(watcher)
        edt_year_register_car.addTextChangedListener(watcher)
        edt_number_register_car.addTextChangedListener(watcher)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == requestImg && data != null && data.data != null) {
            imgUri = data.data
            activity?.let { Glide.with(it).load(imgUri).into(img_car) }

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

    private fun uploadRegister(){
        if (imgUri == null){
            activity?.let { Toasty.error(it, "Pilih gambar mobil terlebih dahulu", Toast.LENGTH_SHORT, true).show() }
            return
        }
        carRegisterViewModel.uploadCarMitra(imageByteArray(bitmapImage),
            edt_car_type_register_car.text.toString().trim(), edt_transmission_register_car.text.toString().trim(),
        edt_year_register_car.text.toString().trim(), edt_number_register_car.text.toString().trim())
    }

    private fun checkUpload(){
        carRegisterViewModel.checkRegisterCar().observe(viewLifecycleOwner, Observer { status->
            if (status){
                context?.let { Toasty.success(it, "Pengajuan mobil berhasil silahkan tunggu pemberitahuan dari admin!", Toast.LENGTH_SHORT, true).show() }
                view?.findNavController()?.navigate(R.id.action_navigation_car_register_to_navigation_dashboard)
            }else{
                context?.let { Toasty.error(it, "Pengajuan mobil gagal silahkan hubungi admin!", Toast.LENGTH_SHORT, true).show() }
            }
        })
    }

}