package com.azhara.perintismitraapp.entity

data class CarData(
    var imgUrl: String? = null,
    var carName: String? = null,
    var statusReady: Boolean? = null,
    var year: Int? = null,
    var price: Long? = null,
    var carId: String? = null
)