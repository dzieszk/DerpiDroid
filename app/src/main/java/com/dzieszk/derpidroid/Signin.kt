package com.dzieszk.derpidroid

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*
import java.util.concurrent.TimeUnit

interface ApiService{

    @POST("/users/sign_in")
    @FormUrlEncoded
    fun SingIn(@Field("utf8") utf: String,
               @Field("authenticity_token") token: String,
               @Field("user[email]") email: String,
               @Field("user[password]") password: String,
               @Field("user[remember_me]") remember: String,
               @Field("commit") commit: String)
}

object ApiUtils {

    val BASE_URL = "https://derpibooru.org"

    val apiService: ApiService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(ApiService::class.java)

}

object RetrofitClient {

    var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        return retrofit

    }
}