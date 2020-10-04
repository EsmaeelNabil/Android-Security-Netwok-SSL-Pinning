package com.example.securedapplication.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.securedapplication.databinding.ActivityMainBinding
import com.example.securedapplication.list.MyListAdapter
import com.example.securedapplication.network.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.net.ssl.SSLPeerUnverifiedException

class ListActivity : AppCompatActivity() {

    private val TAG = "ListActivity"
    private val adapter: MyListAdapter = MyListAdapter()
    private lateinit var binder: ActivityMainBinding
    private fun showLoader() {
        binder.loader.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binder.loader.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)


        binder.recycler.adapter = adapter

        binder.safeCall.setOnClickListener {
            adapter.submitList(listOf())
            showLoader()
            makeSafeCall()
        }

        binder.call.setOnClickListener {
            adapter.submitList(listOf())
            showLoader()
            makeDefaultCall()
        }

    }


    private fun makeDefaultCall() {
        defaultApiService()
            .getDummyList()
            .enqueue(responseHandler)
    }

    private fun makeSafeCall() {
        safeApiService(
            certificates = BaseUrlToPin.setCertificates(
                "sha256/C4f8sd7pdRY7B87OiUo20x3Dh9WVU0ZgJXX67BOKgWw=",
                "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=",
                "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys="
            )
        )
            .getDummyList()
            .enqueue(responseHandler)
    }

    private val responseHandler = object : retrofit2.Callback<DummyListResponse> {
        override fun onResponse(
            call: retrofit2.Call<DummyListResponse>,
            response: retrofit2.Response<DummyListResponse>
        ) {
            if (response.isSuccessful)
                adapter.submitList(response.body())
            else Log.e(TAG, "onResponse: not successful ${response.code()}")

            hideLoader()
        }

        override fun onFailure(
            call: retrofit2.Call<DummyListResponse>,
            t: Throwable
        ) {
            Log.e(TAG, "onFailure: ${t}")
            if (t is SSLPeerUnverifiedException)
                showCustomMessageOnAttackDetected()
            hideLoader()
        }
    }

}



