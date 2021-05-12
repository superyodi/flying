package com.foo.pomodoro.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.foo.pomodoro.utilities.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * The Room database for this app
 */

@Database(entities = arrayOf(Pomodoro::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun pomodoroDao() : PomodoroDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let { database ->
                scope.launch {
                    var pomodoroDao = database.pomodoroDao()

                    // Delete all content here.
                    pomodoroDao.deleteAll()

                    // Add sample words
                    var pomodoro = Pomodoro(
                         "sample title",
                       "공부",
                        5,
                        0

                    )
                    pomodoroDao.insert(pomodoro)

                }
            }
        }


    }

    companion object {

        //For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(
            context: Context,
        scope: CoroutineScope
        ): AppDatabase {
            return instance ?: synchronized(this) {
                val _instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                instance = _instance
                _instance
            }
        }


    }
}

