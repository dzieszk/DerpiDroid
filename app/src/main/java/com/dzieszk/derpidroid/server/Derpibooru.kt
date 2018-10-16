package com.dzieszk.derpidroid.server

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.dzieszk.derpidroid.models.UserStorage
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

class Derpibooru (val context: Context) {

    companion object {
        const val SIGN_IN_URL: String = "https://derpibooru.org/users/sign_in"
        const val ABOUT_URL: String = "https://derpibooru.org/pages/about"
    }

    private val cookieStorage = CookieStorage(context)

    val client = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .followRedirects(false)
        .cookieJar(object: CookieJar{
            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                return cookieStorage.getCookies(url.host()).toMutableList()
            }

            override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                cookieStorage.setCookies(url.host(), cookies)
            }
        })
        .build();

}