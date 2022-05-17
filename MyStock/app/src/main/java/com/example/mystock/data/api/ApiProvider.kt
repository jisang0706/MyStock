package com.example.mystock.data.api

import com.example.mystock.data.api.retrofit.RetrofitCreator
import com.example.mystock.data.api.retrofit.RetrofitService

object ApiProvider {

    private const val baseUrl = "https://api.finance.naver.com"

    fun provideFinanceApi(): RetrofitService = getRetrofitBuilder().create(RetrofitService::class.java)

    private fun getRetrofitBuilder() = RetrofitCreator.defaultRetrofit(baseUrl)
}