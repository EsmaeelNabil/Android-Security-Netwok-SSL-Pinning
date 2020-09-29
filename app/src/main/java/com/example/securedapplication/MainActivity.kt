package com.example.securedapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.securedapplication.databinding.ActivityMainBinding
import com.example.securedapplication.list.MyListAdapter
import com.example.securedapplication.network.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val adapter: MyListAdapter = MyListAdapter()
    private lateinit var binder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)


        binder.recycler.adapter = adapter

        binder.safeCall.setOnClickListener {
            adapter.submitList(listOf())
            makeSafeCall()
        }

        binder.call.setOnClickListener {
            adapter.submitList(listOf())
            makeDefaultCall()
        }

    }

    private fun makeDefaultCall() {
        defaultApiService().getDummyList()
            .enqueue(object : retrofit2.Callback<DummyListResponse> {
                override fun onResponse(
                    call: retrofit2.Call<DummyListResponse>,
                    response: retrofit2.Response<DummyListResponse>
                ) {
                    if (response.isSuccessful)
                        adapter.submitList(response.body())
                    else Log.e(TAG, "onResponse: not successful ${response.code()}")
                }

                override fun onFailure(
                    call: retrofit2.Call<DummyListResponse>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: ", t)
                }

            })
    }

    private fun makeSafeCall() {
        safeApiService(
            certificates = BaseUrlToPin.setCertificates(
                "sha256/C4f8sd7pdRY7B87OiUo20x3Dh9WVU0ZgJXX67BOKgWw=",
                "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=",
                "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys="
            )
        ).getDummyList()
            .enqueue(object : retrofit2.Callback<DummyListResponse> {
                override fun onResponse(
                    call: retrofit2.Call<DummyListResponse>,
                    response: retrofit2.Response<DummyListResponse>
                ) {
                    if (response.isSuccessful)
                        adapter.submitList(response.body())
                    else Log.e(TAG, "safe api onResponse: not successful ${response.code()}")
                }

                override fun onFailure(
                    call: retrofit2.Call<DummyListResponse>,
                    t: Throwable
                ) {
                    Log.e(TAG, "safe api onFailure: ", t)
                }

            })
    }

}



