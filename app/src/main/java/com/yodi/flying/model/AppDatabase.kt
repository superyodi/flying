package com.yodi.flying.model

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yodi.flying.model.dao.PomodoroDao
import com.yodi.flying.model.dao.ReportDao
import com.yodi.flying.model.dao.TagDao
import com.yodi.flying.model.dao.UserDao
import com.yodi.flying.model.entity.*
import com.yodi.flying.utils.Constants.Companion.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * The Room database for this app
 */

@Database(
    entities = [Pomodoro::class, Tag::class, User::class, Report::class, Ticket::class, TagWithTime::class],
    version = 7,
    exportSchema = false)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {


    abstract fun pomodoroDao() : PomodoroDao
    abstract fun tagDao() : TagDao
    abstract fun userDao() : UserDao
    abstract fun reportDao() : ReportDao


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