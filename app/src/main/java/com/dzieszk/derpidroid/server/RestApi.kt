package com.dzieszk.derpidroid.server

import android.app.Application
import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RestApi(val context: Context) {

    companion object {
        fun create(addCookiesInterceptor: AddCookiesInterceptor, receivedCookiesInterceptor: ReceivedCookiesInterceptor): DerpibooruService{
            val client = OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(addCookiesInterceptor)
                .addInterceptor(receivedCookiesInterceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://derpibooru.org")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(DerpibooruService::class.java)
        }
    }
}