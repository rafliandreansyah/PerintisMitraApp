package com.azhara.perintismitraapp.entity

data class Partner(
    var address: String? = null,
    var email: String? = null,
    var imgUrl: String? = null,
    var owner: String? = null,
    var phone: Long? = null,
    var statusActive: Boolean? = false,
    var travelName: String? = null
)