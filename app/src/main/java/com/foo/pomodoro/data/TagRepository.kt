package com.foo.pomodoro.data


import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class TagRepository(private val tagDao: TagDao) {



    val allTags : Flow<List<Tag>> = tagDao.getTags()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(tag: Tag) {
        tagDao.insert(tag)
    }

}