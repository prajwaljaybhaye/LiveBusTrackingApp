package com.litsbros.livebustrackingsystem.backand

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.litsbros.livebustrackingsystem.backand.ApiInterface.AdminApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://livebustrackingapp.mooo.com/apis/admin/"

    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()  // 👈 allows malformed or relaxed JSON parsing
            .create()
    }

    val api: AdminApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AdminApi::class.java)
    }
}
