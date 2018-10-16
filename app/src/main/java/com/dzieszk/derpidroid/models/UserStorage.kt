package com.dzieszk.derpidroid.models

import android.content.Context
import com.google.gson.Gson

class UserStorage(context: Context) {

    companion object {
        private val PREFERENCES_NAME: String = "UserStorage"
    }

    private val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, 0);
    private val mGson = Gson()

    fun setUser(username: String, avatarURL: String) {
        val json = mGson.toJson(User(username, avatarURL));
        sharedPref.edit()
            .putString("user", json)
            .apply();
    }

    fun getUser() : User {
        val json = sharedPref.getString("user", "")

        if(!json.isNullOrEmpty()) return mGson.fromJson(json, User::class.java);
        else return User()
    }
}