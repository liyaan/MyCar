package com.liyaan.car

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liyaan.car.abs.BaseActivity
import com.liyaan.car.adapter.MyPhotoAdapter
import com.liyaan.car.dao.CarBase
import com.liyaan.car.dao.CarInfo
import com.liyaan.car.databinding.ActivityAddBinding
import com.liyaan.car.utils.ImgUtil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AddActivity: BaseActivity() {
    var binding:ActivityAddBinding? = null
    private var mPhotoAdapter:MyPhotoAdapter? = null
    private val listImage:MutableList<String>? = ArrayList()
    var path: String? = null
    private val typeSign:Int by lazy {
        intent.getIntExtra("typeSign",-1)
    }
    private val id:Int by lazy {
        intent.getIntExtra("id",-1)
    }
    private var carModelBean:CarInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.include.titleContent.setText("添加车辆")
        binding!!.include.titleRight.visibility = View.VISIBLE
        binding!!.include.titleRight.setText("确认")
        binding!!.include.titleRight.setOnClickListener {
            addInfo()
        }
        binding!!.include.titleBack.visibility = View.VISIBLE
        binding!!.include.titleBack.setOnClickListener { finish() }
        mPhotoAdapter = MyPhotoAdapter(this,listImage)
        binding!!.mCarGridPhoto.adapter = mPhotoAdapter
        binding!!.mCarGridPhoto.setOnItemClickListener { parent, view, position, id ->
            val posi = listImage!!.size
            if (posi == position && posi<9){
                val file = File(ImgUtil.PATH_IMAGE);
                if (!file.exists()) {
                    file.mkdir()
                }
                path = "${System.currentTimeMillis()}.jpg"
                ImgUtil.choicePhoto(this,path)
            }
        }
        mPhotoAdapter?.let {
            it.setDelLis { index->
                listImage?.let { list->
                    list.remove(list[index])
                }
                it.notifyDataSetChanged()
            }
        }

        if (typeSign==1){
            if (id!=-1){
                initData()
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch(Dispatchers.IO) {
            carModelBean = CarBase.getDatabase(this@AddActivity).userDao().getCarInfo(id)
            launch(Dispatchers.Main) {
                binding?.apply {
                    mCarModel.setText(carModelBean!!.carModel)
                    mCarTime.setText(carModelBean!!.carTime)
                    mCarConfig.setText(carModelBean!!.carConfig)
                    mCarPro.setText(carModelBean!!.carPro)
                    mCarPrice.setText(carModelBean!!.carPrice)
                    mCarAddress.setText(carModelBean!!.carAddress)
//                    carImg = if(listImage!!.size>0) Gson().toJson(listImage) else "",
                    mCarOther.setText(carModelBean!!.carOther)
                    if (carModelBean!!.carImg.isNotEmpty()){
                        val type = object: TypeToken<MutableList<String>>(){}.type
                        listImage?.addAll(Gson().fromJson(carModelBean!!.carImg,type))
                        mPhotoAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    //拍照 选图回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ImgUtil.TAKE_PHOTO->{
                // 创建File对象，用于存储拍照后的图片
                if (resultCode == Activity.RESULT_OK){
                    val file = File(getExternalFilesDir("/images")?.getAbsolutePath() , path)
                    if(!file.exists()){
                        Log.i("aaaaa","aaaaaaaaaaaaaaaaaaaaa")
                    }else{
                        listImage?.add(file.absolutePath)
                        mPhotoAdapter?.notifyDataSetChanged()
                    }
                }

            }
            ImgUtil.CHOOSE_PHOTO->{
//                LogUtil.i(data.toString())
                if (data != null && data.data != null) {
                    val originalUri: Uri? = data.data //获得图片的uri
                    listImage?.add(ImgUtil.getRealPathFromUri(this, originalUri))
                    mPhotoAdapter?.notifyDataSetChanged()
                }
            }


        }
    }
    private fun addInfo(){
        lifecycleScope.launch(Dispatchers.IO) {
            if (typeSign!=1){
                CarBase.getDatabase(this@AddActivity).userDao().insert(
                    CarInfo(
                        carModel = binding!!.mCarModel.text.toString().trim(),
                        carTime = binding!!.mCarTime.text.toString().trim(),
                        carConfig = binding!!.mCarConfig.text.toString().trim(),
                        carPro = binding!!.mCarPro.text.toString().trim(),
                        carPrice = binding!!.mCarPrice.text.toString().trim(),
                        carAddress = binding!!.mCarAddress.text.toString().trim(),
                        carImg = if(listImage!!.size>0) Gson().toJson(listImage) else "",
                        carOther = binding!!.mCarOther.text.toString().trim(),
                        dataTime = "${System.currentTimeMillis()}"
                    )
                )
                launch {
                    val intent = Intent(this@AddActivity, MainActivity::class.java)
                    setResult(0x13,intent)
                    finish()
                }
            }else{
                if (typeSign==1){
                    CarBase.getDatabase(this@AddActivity).userDao().updateCarInfo(id,
                         binding!!.mCarModel.text.toString().trim(),
                         binding!!.mCarTime.text.toString().trim(),
                          binding!!.mCarConfig.text.toString().trim(),
                          binding!!.mCarPro.text.toString().trim(),
                          binding!!.mCarPrice.text.toString().trim(),
                           binding!!.mCarAddress.text.toString().trim(),
                          if(listImage!!.size>0) Gson().toJson(listImage) else "",
                           binding!!.mCarOther.text.toString().trim(),

                    )
                    launch {
                        val intent = Intent(this@AddActivity, MainActivity::class.java)
                        intent.putExtra("typeSign",typeSign)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }
    }
}