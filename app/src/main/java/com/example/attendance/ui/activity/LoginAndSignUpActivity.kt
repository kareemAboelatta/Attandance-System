package com.example.attendance.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginAndSignUpActivity : AppCompatActivity() {

    @Inject
    lateinit var glide : RequestManager


    @Inject
    lateinit var auth: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_reg)
        checkUserState()



    }


    private fun checkUserState(){
        if (auth.currentUser != null ){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}