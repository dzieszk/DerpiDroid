package com.dzieszk.derpidroid.ui

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dzieszk.derpidroid.R
import com.dzieszk.derpidroid.models.User
import com.dzieszk.derpidroid.models.UserStorage
import com.facebook.stetho.Stetho

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences("Cookies", 0) //todo delete
        preferences.edit().clear().apply()

        Stetho.initializeWithDefaults(this)

        val storage = UserStorage(this)
        if(storage.getUser().username.isNotBlank()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
