package com.liyaan.car

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.liyaan.car.abs.BaseActivity
import com.liyaan.car.dao.CarBase
import com.liyaan.car.dao.CarInfo
import com.liyaan.car.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private var list:MutableList<CarInfo>? = ArrayList()
    private var mAdapter: MyAdapter? = null
    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.include.titleContent.setText("车辆信息")
        binding!!.include.titleRight.visibility = View.VISIBLE
        binding!!.include.titleRight.setText("添加车辆")
        binding!!.include.titleRight.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent,0x12)
        }
        initData()
//        list?.apply {
//            if (isNotEmpty()){
//                mCarListView.adapter = MyAdapter(this,this@MainActivity)
//            }
//        }
        mAdapter = MyAdapter(list!!,this@MainActivity)
        binding!!.mCarListView.adapter = mAdapter


    }

    private fun initData(){
        lifecycleScope.launch(Dispatchers.IO) {
            if (list!=null){
                if (list!!.size>0){
                    list!!.clear()
                }
            }
            CarBase.getDatabase(this@MainActivity).userDao().getAllCarInfo()
                ?.let { list?.addAll(it) }
            launch(Dispatchers.Main){
                if (mAdapter!=null){
                    mAdapter?.notifyDataSetChanged()
                }
            }

        }
//        val imgs = ArrayList<String>()
//        imgs.add("https://img2.baidu.com/it/u=1093442490,3100662711&fm=253&fmt=auto&app=120&f=JPEG?w=640&h=480")
//        list.add(CarBean(1,"德龙x3000","21年5月","潍柴发动机431带液缓，带独立空调",
//            "全保险","1.0",imgs,""))
//        list.add(CarBean(2,"德龙x30001","21年6月","潍柴发动机432带液缓，带独立空调",
//            "全保险","1.0",imgs,""))
//        list.add(CarBean(3,"德龙x30002","21年7月","潍柴发动机433带液缓，带独立空调",
//            "全保险","1.0",imgs,""))
//        list.add(CarBean(4,"德龙x30002","21年7月","潍柴发动机434带液缓，带独立空调,潍柴发动机435带液缓",
//            "全保险","1.1",imgs,""))
//        list.add(CarBean(5,"德龙x30002","21年7月","潍柴发动机435带液缓，带独立空调,潍柴发动机434带液缓",
//            "全保险","1.2",imgs,""))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null){
            if (requestCode == 0x12){
                initData()
            }
        }
    }
}
