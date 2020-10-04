package com.example.securedapplication.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.securedapplication.R
import com.example.securedapplication.databinding.ActivityLoginBinding
import com.example.securedapplication.network.*
import javax.net.ssl.SSLPeerUnverifiedException

class LoginActivity : AppCompatActivity() {
    lateinit var binder: ActivityLoginBinding
    private val TAG = "LoginActivity"
    private fun showLoader() {
        binder.loader.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binder.loader.visibility = View.GONE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.defaultLoginButton.setOnClickListener {
            makeCompromisedLogin()
        }

        binder.safeLoginButton.setOnClickListener {
            makeSafeLogin()
        }

    }

    private fun makeCompromisedLogin() {
        showLoader()
        defaultApiService()
            .login()
            .enqueue(responseHandler)
    }

    private fun makeSafeLogin() {
        showLoader()
        safeApiService(
            certificates = BaseUrlToPin.setCertificates(
                "sha256/C4f8sd7pdRY7B87OiUo20x3Dh9WVU0ZgJXX67BOKgWw=",
                "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=",
                "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys="
            )
        )
            .login()
            .enqueue(responseHandler)
    }

    private val responseHandler = object : retrofit2.Callback<UserResponse> {
        override fun onResponse(
            call: retrofit2.Call<UserResponse>,
            response: retrofit2.Response<UserResponse>
        ) {
            if (response.isSuccessful) {
                binder.userItem = response.body()!![0].copy(isDataAvailable = true)
                binder.executePendingBindings()
            } else {
                Log.e(TAG, "onResponse: ${response.errorBody()?.string()}")
                Toast.makeText(
                    applicationContext,
                    "status code : ${response.code()}",
                    Toast.LENGTH_LONG
                ).show()
                resetUi()
            }
            hideLoader()
        }

        override fun onFailure(
            call: retrofit2.Call<UserResponse>,
            t: Throwable
        ) {
            Log.e(TAG, "onFailure: $t")
            if (t is SSLPeerUnverifiedException)
                showCustomMessageOnAttackDetected()
                hideLoader()
        }

    }

    private fun resetUi() {
        binder.dataLayout.visibility = View.GONE
        binder.userItem = UserResponse.UserResponseItem(isDataAvailable = false)
        binder.executePendingBindings()
    }
}


