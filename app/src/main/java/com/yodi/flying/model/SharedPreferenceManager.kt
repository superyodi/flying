package com.yodi.flying.model

import android.content.Context
import com.yodi.flying.utils.Constants.Companion.PREF_FILE_NAME


class SharedPreferenceManager(context: Context) {
    private val preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    fun getString(key: String) : String {
        return preferences.getString(key, "").toString()
    }

    fun setString(key: String, value : String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getLong(key: String) : Long {
        return preferences.getLong(key, -1L)
    }

    fun setLong(key: String, value : Long) {
        preferences.edit().putLong(key, value).apply()
    }






}


