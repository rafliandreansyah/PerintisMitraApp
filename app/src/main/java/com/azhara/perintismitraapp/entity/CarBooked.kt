package com.azhara.perintismitraapp.entity

import com.google.firebase.Timestamp

data class CarBooked(
    var userId: String? = null,
    var carId: String? = null,
    var carName: String? = null,
    var statusBooking: Long? = null,
    var totalPrice: String? = null,
    var userName: String? = null,
    var date: String? = null,
    var time: Timestamp? = null,
    var duration: Long? = null
)