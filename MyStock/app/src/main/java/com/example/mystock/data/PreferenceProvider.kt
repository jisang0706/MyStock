package com.example.mystock.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder

object PreferenceProvider {

    lateinit var prefs: SharedPreferences

    val editor: SharedPreferences.Editor by lazy {
        prefs.edit()
    }

    lateinit var context: Context
}