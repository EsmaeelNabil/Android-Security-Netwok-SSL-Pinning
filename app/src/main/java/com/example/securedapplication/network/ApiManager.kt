package com.example.securedapplication.network

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.securedapplication.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// create certificate builder with list of SHA256
fun String.setCertificates(vararg shas: String): CertificatePinner {
    val builder = CertificatePinner.Builder()
    for (key in shas) {
        builder.add(this, key)
    }
    return builder.build()
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


// easily hacked client
fun defaultApiService(): ApiService {
    val client = OkHttpClient.Builder()
        .addInterceptor(provideLoggingInterceptor())
        .readTimeout(100L, TimeUnit.SECONDS)
        .writeTimeout(100L, TimeUnit.SECONDS)
        .build()

    return creator(client).create(ApiService::class.java)
}

// somehow safe client that is hard to intercept without root
fun safeApiService(certificates: CertificatePinner): ApiService {
    val client = OkHttpClient.Builder()
        .addInterceptor(provideLoggingInterceptor())
        // added certificates to compare at runtime with server's CA
        .certificatePinner(certificates)
        // enough time to be able to edit the response in burp suite
        .readTimeout(100L, TimeUnit.SECONDS)
        .writeTimeout(100L, TimeUnit.SECONDS)
        .build()

    return creator(client).create(ApiService::class.java)
}

fun creator(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BaseUrl)
    .addConverterFactory(GsonConverterFactory.create(Gson()))
    .client(client)
    .build()
