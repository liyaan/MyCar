package com.liyaan.car

data class CarBean(
    val id:Int,
    val carModel:String,
    val carTime:String,
    val carConfig:String,
    val carPro:String,
    val carPrice:String,
    val carImg:MutableList<String>,
    val carOther:String
)
