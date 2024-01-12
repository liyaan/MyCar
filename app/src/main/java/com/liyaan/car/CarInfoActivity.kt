package com.liyaan.car

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liyaan.car.abs.BaseActivity
import com.liyaan.car.adapter.MyInfoPhotoAdapter
import com.liyaan.car.dao.CarBase
import com.liyaan.car.dao.CarInfo
import com.liyaan.car.databinding.ActivityCarInfoBinding
import com.liyaan.car.utils.ShareUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class CarInfoActivity:BaseActivity() {
    private var binding:ActivityCarInfoBinding? = null
    private var mAdapter: MyInfoPhotoAdapter? = null
    private var list:MutableList<String> = ArrayList()
    private var clickNum:Int = 0
    private var carModelBean:CarInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarInfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.include.titleContent.setText("车辆信息")
        binding!!.include.titleBack.visibility = View.VISIBLE
        binding!!.include.titleBack.setOnClickListener { finish() }
        binding!!.include.titleRight.visibility = View.VISIBLE
        binding!!.include.titleRight.setText("分享")
        binding!!.include.titleRight.setOnClickListener {
            if(clickNum==1){
                shareString()
                return@setOnClickListener
            }
            if (list.size>0){
                clickNum = 1
                ShareUtils.shareWechatFriend(this, list[0])
            }else{
                shareString()
            }

        }
        mAdapter = MyInfoPhotoAdapter(this@CarInfoActivity,list)
        binding!!.mInfoCarGridView.adapter = mAdapter
        initData()

    }

    private fun  initData(){
        val id = intent.getIntExtra("id",-1)
        if (id!=-1){
            lifecycleScope.launch(Dispatchers.IO) {
                carModelBean = CarBase.getDatabase(this@CarInfoActivity).userDao().getCarInfo(id)
                launch(Dispatchers.Main){
                    carModelBean?.apply {
                        binding!!.itemCar.itemCarModel.setText(carModel)
                        binding!!.itemCar.itemCarTime.setText(carTime)
                        binding!!.itemCar.itemCarConfig.setText(carConfig)
                        binding!!.itemCar.itemCarPro.setText(carPro)
                        binding!!.itemCar.itemCarPrice.setText(carPrice)
                        binding!!.itemCarAddress.setText(carAddress)
                        binding!!.itemCarOther.setText(carOther)
                        if (carImg.isNotEmpty()){
                            val type = object: TypeToken<MutableList<String>>(){}.type
                            list.addAll(Gson().fromJson(carImg,type))
                            mAdapter?.notifyDataSetChanged()
                        }
                    }

                }

            }
        }

    }
    private fun shareString(){
        clickNum = 0
        carModelBean?.apply {
            val str = StringBuilder()
            str.append("车型:").append(carModel).append("\n")
                .append("车辆配置:").append(carConfig).append("\n")
                .append("上牌时间:").append(carTime).append("\n")
                .append("保险:").append(carPro).append("\n")
                .append("价格:").append(carPrice).append("\n")
            ShareUtils.shareWechatFriendContent(this@CarInfoActivity,str.toString())
        }
    }
}