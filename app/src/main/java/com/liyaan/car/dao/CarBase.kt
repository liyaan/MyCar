package com.liyaan.car.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [CarInfo::class],
    version = 1,
    exportSchema = false
)
abstract class CarBase : RoomDatabase() {

    abstract fun userDao(): CarDao
    private class TodoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onOpen(db)
//            INSTANCE?.let { database ->
//                scope.launch {
//                    var todoDao = database.userDao()
//
//                    // Delete all content here.
//                    todoDao.deleteAll()
//
//                    // Add sample todos.
////                    var todo = Todo(0,"title","content")
////                    todoDao.insert(todo)
////                    todo = Todo(0,"title1","content1")
////                    todoDao.insert(todo)
//
//                    // TODO: Add your own words!
//                    todo = Todo(0,"title2","content2")
//                    todoDao.insert(todo)
//                }
//            }
        }
    }

    companion object {
//        val instance = Room.databaseBuilder(context, CarBase::class.java, "car_db").build()
        @Volatile
        private var INSTANCE: CarBase? = null

//        fun getDatabase(context: Context, scope: CoroutineScope): CarBase{
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    CarBase::class.java,
//                    "todo_database"
//                ).addCallback(TodoDatabaseCallback(scope)).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
        fun getDatabase(context: Context): CarBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarBase::class.java,
                    "car_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}