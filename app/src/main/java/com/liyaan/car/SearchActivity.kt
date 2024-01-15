package com.liyaan.car

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.liyaan.car.abs.BaseActivity
import com.liyaan.car.dao.CarBase
import com.liyaan.car.dao.CarInfo
import com.liyaan.car.databinding.ActivitySearchLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity:BaseActivity() {
    private val binding:ActivitySearchLayoutBinding by lazy {
        ActivitySearchLayoutBinding.inflate(layoutInflater)
    }
    private val type:Int by lazy {
        intent.getIntExtra("type",0)
    }
    private val list:MutableList<CarInfo> = ArrayList()
    private val mAdapter: MyAdapter by lazy {
        MyAdapter(list,this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.include.titleContent.setText("搜索页面")
        binding.include.titleRight.visibility = View.VISIBLE

        binding.include.titleBack.visibility = View.VISIBLE
        binding.include.titleBack.setOnClickListener { finish() }

        binding.mSearchBtn.setOnClickListener {
            val content = binding.mSearchContent.text.toString()
            if (content.isEmpty()) return@setOnClickListener
            if (list.size>0){
                list.clear()
            }
            when(type){
                1->{
                    lifecycleScope.launch(Dispatchers.IO) {
                        CarBase.getDatabase(this@SearchActivity).userDao().getDateTimeCarInfo(content)
                            ?.let { list.addAll(it) }
                        notifyData()

                    }
                }
                2->{
                    lifecycleScope.launch(Dispatchers.IO) {
                        CarBase.getDatabase(this@SearchActivity).userDao().getModelCarInfo(content)
                            ?.let { list.addAll(it) }
                        notifyData()
                    }
                }
                3->{
                    lifecycleScope.launch(Dispatchers.IO) {
                        CarBase.getDatabase(this@SearchActivity).userDao().getPriceCarInfo(content)
                            ?.let { list.addAll(it) }
                        notifyData()
                    }
                }
                4->{
                    lifecycleScope.launch(Dispatchers.IO) {
                        CarBase.getDatabase(this@SearchActivity).userDao().getConfigCarInfo(content)
                            ?.let { list.addAll(it) }
                        notifyData()
                    }
                }
                else->{
                    notifyData()
                }
            }
        }
        binding.mSearchRecyclerView.adapter = mAdapter
    }
    private fun notifyData(){
        lifecycleScope.launch(Dispatchers.Main){
            mAdapter?.notifyDataSetChanged()
        }
    }
}