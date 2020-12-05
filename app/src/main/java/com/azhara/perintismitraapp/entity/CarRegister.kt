package com.azhara.perintismitraapp.entity

data class CarRegister (
    var partnerId: String? = null,
    var carType: String? = null,
    var carTransmission: String? = null,
    var carYear: Long? = null,
    var carNumber: String? = null,
    var carImage: String? = null
)