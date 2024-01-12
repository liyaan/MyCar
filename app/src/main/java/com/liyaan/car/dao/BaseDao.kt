package com.liyaan.car.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert
    fun insert(bean: T)

    @Insert
    fun insertAll(bean: T)

    @Delete
    fun delete(bean:T)

    @Update
    fun update(bean: T)

}