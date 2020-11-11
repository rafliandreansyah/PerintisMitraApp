package com.azhara.perintismitraapp.entity

data class PartnerRegister (
    var email: String? = null,
    var name: String? = null,
    var address: String? = null,
    var phoneNumber: Long? = null,
    var carType: String? = null,
    var carYear: Int? = null,
    var carNumberRegister: String? = null,
    var carTransmision: Int? = null,
    var carImg: String? = null
)