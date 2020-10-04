package com.example.securedapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.securedapplication.list.ListActivity
import com.example.securedapplication.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        listButton?.setOnClickListener { startActivity(Intent(this@HomeActivity,ListActivity::class.java)) }
        loginButton?.setOnClickListener { startActivity(Intent(this@HomeActivity,LoginActivity::class.java)) }
    }
}