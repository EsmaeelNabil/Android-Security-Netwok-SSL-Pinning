package com.example.securedapplication.network


import com.example.securedapplication.R
import retrofit2.Call
import retrofit2.http.GET
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.clear
import coil.load


interface ApiService {
    @GET("dummyList")
    fun getDummyList(): Call<DummyListResponse>

    @GET("login")
    fun login(): Call<UserResponse>
}

class UserResponse : ArrayList<UserResponse.UserResponseItem>() {
    data class UserResponseItem(
        var isDataAvailable: Boolean = false,
        val avatar: String = "", // https://s3.amazonaws.com/uifaces/faces/twitter/ponchomendivil/128.jpg
        val createdAt: String = "", // 2020-09-30T21:53:43.925Z
        val id: String = "", // 1
        val isAdmin: Boolean = false, // false
        val isPremium: Boolean = false, // false
        val name: String = "" // Brain Rippin
    )
}

class DummyListResponse : ArrayList<DummyListResponse.DummyListResponseItem>() {
    data class DummyListResponseItem(
        val avatar: String? = "", // https://s3.amazonaws.com/uifaces/faces/twitter/SlaapMe/128.jpg
        val createdAt: String? = "", // 2020-09-29T20:49:02.031Z
        val id: String? = "", // 16
        val name: String? = "" // Deon Shields
    )
}


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    url?.let {
        view.load(url) {
            placeholder(R.color.green)
            error(R.color.colorAccent)
        }
    }?: run { view.clear() }
}
