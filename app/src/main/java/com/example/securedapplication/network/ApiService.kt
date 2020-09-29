package com.example.securedapplication.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("dummyList")
    fun getDummyList(): Call<DummyListResponse>
}

class DummyListResponse : ArrayList<DummyListResponse.DummyListResponseItem>(){
    data class DummyListResponseItem(
        val avatar: String? = "", // https://s3.amazonaws.com/uifaces/faces/twitter/SlaapMe/128.jpg
        val createdAt: String? = "", // 2020-09-29T20:49:02.031Z
        val id: String? = "", // 16
        val name: String? = "" // Deon Shields
    )
}
