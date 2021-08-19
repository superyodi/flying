package com.yodi.flying.model.repository


import androidx.annotation.WorkerThread
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.TagDao
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TagRepository(private val tagDao: TagDao,
                    private val preferences: SharedPreferenceManager) {


    fun getTags(): Flow<List<Tag>> = tagDao.getTags(preferences.getLong(Constants.PREF_USER_ID))


   fun insert(tag: String) = CoroutineScope(IO).launch  {
       tagDao.insert(Tag(tag, preferences.getLong(Constants.PREF_USER_ID)))
   }



}