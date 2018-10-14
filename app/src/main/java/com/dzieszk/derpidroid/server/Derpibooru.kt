package com.dzieszk.derpidroid.server

import android.content.Context
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

class Derpibooru (context: Context) {

    companion object {
        const val URL: String = "https://derpibooru.org/users/sign_in"
    }

    private val cookieStorage = CookieStorage(context)

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .followRedirects(false)
        .cookieJar(object: CookieJar{
            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                Log.d("XD - get cookies", cookieStorage.getCookies(url.host()).toString())
                return cookieStorage.getCookies(url.host()).toMutableList()
            }

            override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                Log.d("XD - set cookies", cookies.toString())
                cookieStorage.setCookies(url.host(), cookies)
            }
        })
        .build();

    fun requestAuthenticityToken(email: String, password: String){
        val authencityTokenRequest: Request = Request.Builder()
            .url(URL)
            .build();

        client.newCall(authencityTokenRequest).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val doc = Jsoup.parse(response.body()?.string())
                val metaHeaders = doc.select("head").first().select("meta");
                for(header: Element in metaHeaders){
                    if(header.attr("name").equals("csrf-token")){
                        Log.d("XD - csrf", header.attr("content"))
                        login(email, password, header.attr("content"))
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XD", "failure")
            }
        })
    }

    fun login(email: String, password: String, token: String){
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("utf8","✓")
            .addFormDataPart("authenticity_token", token)
            .addFormDataPart("user[email", email)
            .addFormDataPart("user[password]", password)
            .addFormDataPart("user[remember_me]", "1")
            .addFormDataPart("commit", "Sign_in")
            .build()

        val loginRequest = Request.Builder()
            .url(URL)
            .post(requestBody)
            .build()

        client.newCall(loginRequest).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.d("XD - login response", response.toString())
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XD", "login failure")
            }
        })
    }

}