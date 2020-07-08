package com.cuncisboss.githubuserfinal.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.cuncisboss.githubuserfinal.data.model.DateTime
import com.cuncisboss.githubuserfinal.util.Constants.KEY_TIME
import com.cuncisboss.githubuserfinal.util.Constants.PREF_NAME

class GithubPref {
    companion object {

        private fun getSharedPreference(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        }

        fun setTime(context: Context, time: String) {
            getSharedPreference(context).edit {
                putString(KEY_TIME, time)
            }
        }

        fun getTime(context: Context): DateTime {
            val time = getSharedPreference(context).getString(KEY_TIME, "").toString()
            val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return DateTime(timeArray[0], timeArray[1])
        }

        fun getStrTime(context: Context): String {
            return getSharedPreference(context).getString(KEY_TIME, "").toString()
        }

        fun clear(context: Context) {
            getSharedPreference(context).edit {
                clear()
            }
        }
    }
}