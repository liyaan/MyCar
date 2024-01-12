package com.liyaan.car

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.liyaan.car.dao.CarInfo

class MyAdapter(private val list: MutableList<CarInfo>, private val context:Context): RecyclerView.Adapter<MyAdapter.Vh>() {
    private val mLayoutInflater:LayoutInflater = LayoutInflater.from(context)
    private val requestOption: RequestOptions = RequestOptions()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .fallback(R.mipmap.ic_launcher)

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemCarImg: AppCompatImageView = itemView.findViewById(R.id.itemCarImg)
        val itemCarModel: AppCompatTextView = itemView.findViewById(R.id.itemCarModel)
        val itemCarTime: AppCompatTextView = itemView.findViewById(R.id.itemCarTime)
        val itemCarConfig: AppCompatTextView = itemView.findViewById(R.id.itemCarConfig)
        val itemCarPro: AppCompatTextView = itemView.findViewById(R.id.itemCarPro)
        val itemCarPrice: AppCompatTextView = itemView.findViewById(R.id.itemCarPrice)
        val carLayout: RelativeLayout = itemView.findViewById(R.id.carLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(mLayoutInflater.inflate(R.layout.item_car,parent,false))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        if (holder!=null){
            val carModelBean = list.get(position)
            holder.itemCarModel.setText(carModelBean.carModel)
            holder.itemCarTime.setText(carModelBean.carTime)
            holder.itemCarConfig.setText(carModelBean.carConfig)
            holder.itemCarPro.setText(carModelBean.carPro)
            holder.itemCarPrice.setText(carModelBean.carPrice)
//            Glide.with(context)
//                .load(carModelBean.carImg[0])
//                .apply(requestOption)
//                .into(holder.itemCarImg)
            holder.carLayout.setOnClickListener {
                val intent = Intent(context,CarInfoActivity::class.java)
                intent.putExtra("id",carModelBean.id)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}