package com.dzieszk.derpidroid.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dzieszk.derpidroid.R
import com.dzieszk.derpidroid.server.Derpibooru
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var derpi: Derpibooru? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Stetho.initializeWithDefaults(this)

        this.derpi = Derpibooru(this)

        email_sign_in_button.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()

            derpi?.requestAuthenticityToken(email, password)
        }
    }
}
