package com.yodi.flying.model

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yodi.flying.model.dao.PomodoroDao
import com.yodi.flying.model.dao.TagDao
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.utils.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * The Room database for this app
 */

@Database(entities = arrayOf(Pomodoro::class, Tag::class), version = 2, exportSchema = false)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {


    abstract fun pomodoroDao() : PomodoroDao
    abstract fun tagDao() : TagDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var tagDao = database.tagDao()


                    // Add sample tags.
                    tagDao.insert(Tag("프로젝트"))
                    tagDao.insert(Tag("안드로이드"))
                    tagDao.insert(Tag("디자인"))
                    tagDao.insert(Tag("토익"))


                }
            }
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