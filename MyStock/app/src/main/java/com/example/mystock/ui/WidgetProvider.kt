package com.example.mystock.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.mystock.R
import com.example.mystock.data.api.ApiProvider
import com.example.mystock.data.api.retrofit.RetrofitCreator
import com.example.mystock.data.api.retrofit.RetrofitService
import com.example.mystock.data.dto.FinanceDto
import com.example.mystock.ui.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WidgetProvider : AppWidgetProvider() {

    private var financeCall: Call<FinanceDto>? = null

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds?.forEach { appWidgetId ->
            addViews(context, appWidgetManager, appWidgetId)
        }
    }

    private fun addViews(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int) {
        var stockCodes: Map<String, String> = mutableMapOf(Pair("삼성전자", "005930"),
            Pair("SK하이닉스", "000660"), Pair("기아", "000270"))

        val views = RemoteViews(context?.packageName, R.layout.widget_stock)

        val prefs = context!!.getSharedPreferences("myStock", MODE_PRIVATE)

        val prefs_data: String = prefs.getString("widgetStock", "").toString()
        if (!prefs_data.equals("")) {
            val financeApi = RetrofitCreator.defaultRetrofit("https://api.finance.naver.com")
                .create(RetrofitService::class.java)
            financeCall = financeApi.getFinance(stockCodes[prefs_data]!!)
            financeCall?.enqueue(object: Callback<FinanceDto> {
                override fun onResponse(call: Call<FinanceDto>, response: Response<FinanceDto>) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        views.setTextViewText(R.id.stock_title_text, prefs_data)
                        views.setTextViewText(R.id.now_cost, body.now.toString())
                        views.setImageViewResource(R.id.stock_arrow_image, when(body.risefall) {
                            1 -> R.drawable.much_up_arrow
                            2 -> R.drawable.little_up_arrow
                            3 -> R.drawable.common_arrow
                            4 -> R.drawable.much_down_arrow
                            5 -> R.drawable.little_down_arrow
                            else -> R.drawable.common_arrow
                        })
                        views.setImageViewResource(R.id.character_image, when(body.risefall) {
                            1 -> R.drawable.much_up
                            2 -> R.drawable.little_up
                            3 -> R.drawable.common
                            4 -> R.drawable.much_down
                            5 -> R.drawable.little_down
                            else -> R.drawable.common
                        })
                        appWidgetManager?.updateAppWidget(appWidgetId, views)
                    }
                }

                override fun onFailure(call: Call<FinanceDto>, t: Throwable) {
                }

            })
        }
    }
}