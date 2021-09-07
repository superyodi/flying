package com.yodi.flying.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.Report
import com.yodi.flying.model.entity.Task
import com.yodi.flying.model.entity.Ticket

@Dao
interface TicketDao {
    @Query("SELECT * FROM tickets WHERE userId = :userId AND date = :date AND endTime IS NOT NULL")
    suspend fun getTickets(userId : Long, date: Long): List<Ticket>


    @Query("SELECT * FROM tickets WHERE userId = :userId AND date = :date ORDER BY depth DESC LIMIT 1")
    suspend fun getLatestTicket(userId : Long, date: Long): Ticket?

    @Query("SELECT startTime FROM tickets WHERE userId = :userId AND date = :date AND depth = :depth")
    suspend fun getStartTime(userId : Long, date: Long, depth : Int): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: Ticket)

    @Update
    suspend fun update(ticket: Ticket) : Int

}