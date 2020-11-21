package com.azhara.perintismitraapp.home.car.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintismitraapp.R
import com.azhara.perintismitraapp.entity.CarData
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list_car.view.*

class ListCarAdapter : ListAdapter<CarData, ListCarAdapter.ListCarViewHolder>(DIFF_CALLBACK){

    companion object{
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<CarData>(){
            override fun areItemsTheSame(oldItem: CarData, newItem: CarData): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: CarData, newItem: CarData): Boolean = oldItem.carName == newItem.carName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListCarAdapter.ListCarViewHolder = ListCarViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_car, parent, false))

    override fun onBindViewHolder(holder: ListCarAdapter.ListCarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListCarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(data: CarData){
            with(itemView){
                Glide.with(itemView).load(data.imgUrl).into(img_item_car)
                tv_item_car_name.text = data.carName
                tv_item_car_year.text = data.year.toString()
                tv_item_car_price.text = "Rp. ${data.price}/day"
                if (data.statusReady == true){
                    tv_item_car_status.text = "Ready"
                    tv_item_car_status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                }else{
                    tv_item_car_status.text = "Disabled"
                    tv_item_car_status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    tv_item_car_status.elevation = 8F
                }
            }
        }
    }

}