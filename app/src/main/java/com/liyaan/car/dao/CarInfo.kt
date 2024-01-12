package com.liyaan.car.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CarInfo")
data  class CarInfo (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "carModel")
    val carModel: String,
    @ColumnInfo(name = "carTime")
    val carTime: String,
    @ColumnInfo(name = "carConfig")
    val carConfig: String,
    @ColumnInfo(name = "carPro")
    val carPro: String,
    @ColumnInfo(name = "carPrice")
    val carPrice: String,
    @ColumnInfo(name = "carAddress")
    val carAddress: String,
    @ColumnInfo(name = "carImg")
    val carImg: String,
    @ColumnInfo(name = "carOther")
    val carOther: String,
    @ColumnInfo(name = "dataTime")
    val dataTime: String


)