package com.azhara.perintismitraapp.home.carbooked.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.entity.CarBooked
import kotlinx.android.synthetic.main.item_list_car_booked.view.*
import java.text.SimpleDateFormat
import java.util.*

class CarBookedAdapter : ListAdapter<CarBooked, CarBookedAdapter.CarBookedViewHolder>(DIFF_UTIL){
    companion object{
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<CarBooked>(){
            override fun areItemsTheSame(oldItem: CarBooked, newItem: CarBooked): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: CarBooked, newItem: CarBooked): Boolean = oldItem.userName == newItem.userName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarBookedAdapter.CarBookedViewHolder = CarBookedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_car_booked, parent, false))

    override fun onBindViewHolder(holder: CarBookedAdapter.CarBookedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarBookedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(data: CarBooked){
            val timeStampConvert = data.time?.seconds?.let { convertFirebaseToLocalDate(it) }
            val date = timeStampConvert?.let { getDate(it) }
            val time = timeStampConvert?.let { getTime(it) }
            with(itemView){
                tv_item_car_booked_car.text = data.carName
                tv_item_car_booked_customer_name.text = data.userName
                tv_item_car_booked_date.text = date
                tv_item_car_booked_duration.text = data.duration.toString()
                tv_item_car_booked_time.text = time
                tv_item_car_booked_total_price.text = "Rp. ${data.totalPrice}"
                if (data.statusBooking?.toInt() == null){
                    tv_item_car_booked_status_payment.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                    tv_item_car_booked_status_payment.text = "Menunggu Konfirmasi"
                }else if (data.statusBooking?.toInt() == 0) {
                    tv_item_car_booked_status_payment.text = "On Progress"
                }else{
                    tv_item_car_booked_status_payment.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    tv_item_car_booked_status_payment.text = "Selesai"
                }

            }
        }
    }

    private fun convertFirebaseToLocalDate(date: Long): String {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        sdf.timeZone = tz
        val startSecondDate = Date(date * 1000)
        return sdf.format(startSecondDate)
    }

    private fun getDate(date: String): String{
        val dateSplit = date.split(" ")
        return dateSplit[0]
    }

    private fun getTime(date: String): String{
        val timeSplit = date.split(" ")
        return timeSplit[1]
    }
}