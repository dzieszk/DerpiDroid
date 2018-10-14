package com.dzieszk.derpidroid.server

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Cookie

class CookieStorage(context: Context) {

    companion object {
        private val PREFERENCES_NAME: String = "Cookies"
    }

    private val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, 0);
    private val mGson = Gson()

    fun setCookies(domain: String, cookies: List<Cookie>) {
        val json = mGson.toJson(cookies, List::class.java)
        sharedPref.edit()
            .putString(domain, json)
            .apply()
    }

    fun getCookies(domain: String) : List<Cookie> {
        val json = sharedPref.getString(domain, "")

        val datasetListType = object: TypeToken<Collection<Cookie>>() {}.type
        val list = mGson.fromJson<List<Cookie>>(json, datasetListType)
        return if(list != null) list else listOf()
    }
}