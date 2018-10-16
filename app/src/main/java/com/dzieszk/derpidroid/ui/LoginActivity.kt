package com.dzieszk.derpidroid.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Toast
import com.dzieszk.derpidroid.R
import com.dzieszk.derpidroid.models.UserStorage
import com.dzieszk.derpidroid.server.Derpibooru
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private var derpi: Derpibooru? = null
    private var userStorage: UserStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.derpi = Derpibooru(this)
        this.userStorage = UserStorage(this)

        email_sign_in_button.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()

            requestAuthenticityToken(email, password)
        }
    }

    fun requestAuthenticityToken(email: String, password: String){
        val authencityTokenRequest: Request = Request.Builder()
            .url(Derpibooru.SIGN_IN_URL)
            .build();

        derpi?.client?.newCall(authencityTokenRequest)?.enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val doc = Jsoup.parse(response.body()?.string())
                val metaHeaders = doc.select("head").first().select("meta");
                for(header: Element in metaHeaders){
                    if(header.attr("name").equals("csrf-token")){
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
            .addFormDataPart("user[email]", email)
            .addFormDataPart("user[password]", password)
            .addFormDataPart("user[remember_me]", "1")
            .addFormDataPart("commit", "Sign_in")
            .build()

        val loginRequest = Request.Builder()
            .url(Derpibooru.SIGN_IN_URL)
            .post(requestBody)
            .build()

        derpi?.client?.newCall(loginRequest)?.enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("XD", response.code().toString())
                if(response.code() == 302) getUsername()
                else {
                    runOnUiThread {
                        Snackbar.make(root_layout, "Wrong username or password.", Snackbar.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XD", "login failure")
            }
        })
    }

    fun getUsername(){
        val userRequest = Request.Builder()
            .url(Derpibooru.ABOUT_URL)
            .build();

        derpi?.client?.newCall(userRequest)?.enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val doc = Jsoup.parse(response.body()?.string())
                val username = doc.getElementsByClass("header__link-user").first().attr("href").substringAfterLast("/")
                val avatarURL = doc.getElementsByAttribute("alt").first().attr("src").drop(2)
                userStorage?.setUser(username, avatarURL)

                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XD", "failure")
            }
        })
    }
}
