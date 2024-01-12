package com.liyaan.car.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CarDao: BaseDao<CarInfo> {

    /**
     * 查询表里所有数据
     */
    @Query("select * from CarInfo ORDER BY ID DESC")
    fun getAllCarInfo(): MutableList<CarInfo>?

    @Query("select * from CarInfo where id = :id")
    fun getCarInfo(id:Int): CarInfo?

    @Query("select * from CarInfo where dataTime LIKE '%' || :dataTime || '%'")
    fun getMhCarInfo(dataTime:String): MutableList<CarInfo>?

    @Query("select * from CarInfo where dataTime between :minTime and :maxTime")
    fun getPersonByChosen(minTime: Long, maxTime: Long): List<CarInfo?>?
    /**
     * 根据字段删除记录
     */
    @Query("delete from CarInfo where id = :id")
    fun deleteByNum(id: Int)


    /**
     * 修改指定用户的密码
     */
    @Query("update CarInfo set id =:id where carModel =:carModel ")
    fun updatePwd(id: Int, carModel: String)
}