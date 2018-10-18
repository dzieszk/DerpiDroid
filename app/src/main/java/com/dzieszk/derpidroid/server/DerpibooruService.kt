package com.dzieszk.derpidroid.server

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface DerpibooruService {

    @GET("/users/sign_in")
    fun getAuthenticityToken() : Observable<ResponseBody>

    @GET("/pages/about")
    fun getAbout() : Observable<ResponseBody>

    @FormUrlEncoded
    @POST("users/sign_in")
    fun signIn(
        @Field("utf8") utf: String = "✓",
        @Field("authenticity_token") token: String,
        @Field("user[email]") email: String,
        @Field("user[password]") password: String,
        @Field("user[remember_me") remember: String = "1",
        @Field("commit") commit: String = "Sign_in"
    ) : Observable<ResponseBody>

    @GET("/search.json")
    fun searchQuery(@Query("q") query: String) : Observable<ResponseBody>
}
