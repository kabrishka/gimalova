package com.example.movies_top.api

import com.example.movies_top.utils.Constants.BASE_URL_V2_1
import com.example.movies_top.utils.Constants.BASE_URL_V2_2
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var retrofit: Retrofit

    fun getClientV22(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_V2_2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun getClientV21(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_V2_1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}