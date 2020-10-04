package com.example.securedapplication.network

import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.securedapplication.BuildConfig
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


// easily hacked client
fun FragmentActivity.defaultApiService(): ApiService {
    val client = OkHttpClient.Builder()
        .addInterceptor(provideLoggingInterceptor())
        .addInterceptor(ChuckerInterceptor(this))
        .readTimeout(100L, TimeUnit.SECONDS)
        .writeTimeout(100L, TimeUnit.SECONDS)
        .build()

    return creator(client).create(ApiService::class.java)
}

// somehow safe client that is hard to intercept without root
fun FragmentActivity.safeApiService(certificates: CertificatePinner): ApiService {
    val client = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor(this))
        .addInterceptor(provideLoggingInterceptor())
        // added certificates to compare at runtime with server's CA
        .certificatePinner(certificates)
        // enough time to be able to edit the response in burp suite
        .readTimeout(100L, TimeUnit.SECONDS)
        .writeTimeout(100L, TimeUnit.SECONDS)
        .build()

    return creator(client).create(ApiService::class.java)
}

// create certificate builder with list of SHA256
fun String.setCertificates(vararg shas: String): CertificatePinner {
    val builder = CertificatePinner.Builder()
    for (key in shas) {
        builder.add(this, key)
    }
    return builder.build()
}


val attackMessage = "I am aware that Requests is being redirected so, mitm Attack Got blocked "
fun FragmentActivity.showCustomMessageOnAttackDetected() {
    Snackbar.make(this.window.decorView.rootView, attackMessage, Snackbar.LENGTH_LONG).also {
        it.view.apply {
            setBackgroundColor(Color.RED)
            layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                gravity = Gravity.BOTTOM
                setPadding(80)
            }
        }
        it.show()
    }
}


val BaseUrl = "https://5f73aa71b63868001615fbe8.mockapi.io/"
val BaseUrlToPin = "5f73aa71b63868001615fbe8.mockapi.io"
val BaseUrlToGenerateFrom = "www.google.com"

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }
}


fun creator(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BaseUrl)
    .addConverterFactory(GsonConverterFactory.create(Gson()))
    .client(client)
    .build()
