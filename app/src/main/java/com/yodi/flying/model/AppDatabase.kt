package com.yodi.flying.model

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yodi.flying.model.dao.PomodoroDao
import com.yodi.flying.model.dao.TagDao
import com.yodi.flying.model.dao.UserDao
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.model.entity.User
import com.yodi.flying.utils.Constants.Companion.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * The Room database for this app
 */

@Database(entities = arrayOf(Pomodoro::class, Tag::class, User::class), version = 4, exportSchema = false)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {


    abstract fun pomodoroDao() : PomodoroDao
    abstract fun tagDao() : TagDao
    abstract fun userDao() : UserDao


    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}