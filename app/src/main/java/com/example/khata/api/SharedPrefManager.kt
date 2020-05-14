package com.example.khata.api

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager{
    private const val SHARED_NAME = "current_user"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(SHARED_NAME, MODE)
    }

    private fun SharedPreferences.edit(operation:(SharedPreferences.Editor)-> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var user: User?
        get() = User(
            preferences.getString("token", "")!!,
            preferences.getString("mobile", "")!!,
            preferences.getString("upi", "")!!
        )

        set(user) = preferences.edit{
            it.putString("mobile", user?.mobile)
            it.putString("token", user?.token)
            it.putString("upi", user?.upi)
        }

    var userToken: String? = null
        get() = preferences.getString("token","")

    fun clear() {
        preferences.edit().clear().apply()
    }
}
