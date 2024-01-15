package com.liyaan.car.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CarDao: BaseDao<CarInfo> {

    /**
     * 查询表里所有数据
     */
    @Query("select * from CarInfo ORDER BY ID DESC LIMIT 50")
    fun getAllCarInfo(): MutableList<CarInfo>?

    @Query("select * from CarInfo where id = :id")
    fun getCarInfo(id: Int): CarInfo?

    @Query("select * from CarInfo where carTime LIKE '%' || :carTime || '%' ORDER BY ID DESC LIMIT 50")
    fun getDateTimeCarInfo(carTime: String): MutableList<CarInfo>?


    @Query("select * from CarInfo where carModel LIKE '%' || :carModel || '%' ORDER BY ID DESC LIMIT 50")
    fun getModelCarInfo(carModel: String): MutableList<CarInfo>?

    @Query("select * from CarInfo where carPrice LIKE '%' || :carPrice || '%' ORDER BY ID DESC LIMIT 50")
    fun getPriceCarInfo(carPrice: String): MutableList<CarInfo>?

    @Query("select * from CarInfo where carConfig LIKE '%' || :carConfig || '%' ORDER BY ID DESC LIMIT 50")
    fun getConfigCarInfo(carConfig: String): MutableList<CarInfo>?

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
    @Query("update CarInfo set carModel =:carModel,carTime=:carTime,carConfig=:carConfig,carPro=:carPro,carPrice=:carPrice,carAddress=:carAddress,carImg=:carImg,carOther=:carOther" +
            " where id =:id ")
    fun updateCarInfo(id: Int,
                  carModel: String,
                  carTime:String,
                  carConfig:String,
                  carPro:String,
                  carPrice:String,
                  carAddress:String,
                  carImg:String,
                  carOther:String)

}


//
//    @Insert
//    fun insert(vararg bean: VideoModel)
//
//    @Delete
//    fun delete(vararg bean: VideoModel):Int
//
//    @Update
//    fun update(vararg bean: VideoModel):Int
//
//    //插入列表
//    @Insert
//    fun insert(beanList: List<VideoModel>)
//
//    //查询整张表，升序排序
//    @Query("SELECT * FROM video_path_table ORDER BY uid ASC")
//    fun getAlphabetizedWords(): LiveData<List<VideoModel>>
//
//    @Query("SELECT * FROM video_path_table WHERE name LIKE :name AND "+
//            "image_path LIKE:imagePath LIMIT 1")
//    fun findByPathNameAndImagePath(name:String,imagePath:String): VideoModel
//
//    @Query("SELECT * FROM video_path_table WHERE video_path= :video_path ")
//    fun findByPathName(video_path:String): VideoModel
//
//    @Query("SELECT * FROM video_path_table")
//    fun queryAllVideo():MutableList<VideoModel>
//
//    @Query("DELETE FROM video_path_table WHERE name= :name ")
//    fun deleteByPathName(name:String)
//
//    @Query("DELETE FROM video_path_table ")
//    fun deleteAll()
//
//    // //查询某项的全部数据（模糊查询）
//    //    @Query("SELECT * FROM 表名 WHERE 某一项 LIKE '%' || :name || '%'")
//    @Query("DELETE FROM video_path_table WHERE name LIKE '%' || :name || '%'")
//    fun delLikeFilePath(name:String)
//
//    @Query("SELECT * FROM video_path_table WHERE name LIKE '%' || :name || '%'")
//    fun selLikeFilePath(name:String):MutableList<VideoModel>
//
//    //@Query("SELECT * FROM user LIMIT :limit")
//    @Query("SELECT * FROM video_path_table LIMIT :limit")
//    fun findLimitFile(limit:Int):MutableList<VideoModel>
//
//    @Query("SELECT COUNT(*) FROM video_path_table")
//    fun getAllVideo():Int
//
//    //分页查询 offset代表从第几条记录“之后“开始查询，limit表明查询多少条结果
//    @Query("SELECT * FROM video_path_table limit :limit offset :offset")
//    fun queryPageVideo(limit:Int,offset:Int):MutableList<VideoModel>