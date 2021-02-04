package com.foo.pomodoro.data

import androidx.room.*
import java.util.*


@Entity(
        tableName = "pomodoro_creating",
        foreignKeys = [
            ForeignKey(entity = Pomodoro::class, parentColumns = ["id"], childColumns = ["pomodoro_id"])
        ],
        indices = [Index("pomodoro_id")]
)
data class PomodoroCreating(
        @ColumnInfo(name = "pomodoro_id") val pomodoroId: String,

        @ColumnInfo(name = "created_date") val createdDate: Calendar = Calendar.getInstance(),

        @ColumnInfo(name = "due_date") val dueDate: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var pomodoroCreatingId: Long = 0


}
