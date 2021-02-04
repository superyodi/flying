package com.foo.pomodoro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.foo.pomodoro.utilities.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * The Room database for this app
 */

@Database(entities = arrayOf(Pomodoro::class), version = 1, exportSchema = false)
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
                        pomodoroId = 0,
                        title = "sample title",
                        description = "sample description",
                        goalCount = 5,
                        nowCount = 0
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

