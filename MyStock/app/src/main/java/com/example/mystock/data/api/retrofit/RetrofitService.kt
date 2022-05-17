package com.example.mystock.data.api.retrofit

import com.example.mystock.data.dto.FinanceDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("service/itemSummary.nhn")
    fun getFinance (
        @Query("itemcode", encoded = true)  itemcode: String
    ) : Call<FinanceDto>
}