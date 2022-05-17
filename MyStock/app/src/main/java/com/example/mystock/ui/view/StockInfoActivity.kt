package com.example.mystock.ui.view

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mystock.R
import com.example.mystock.data.PreferenceProvider
import com.example.mystock.data.StockListProvider
import com.example.mystock.data.dto.FinanceDto
import com.example.mystock.databinding.ActivityStockInfoBinding
import com.example.mystock.ui.WidgetProvider

class StockInfoActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityStockInfoBinding
    private val binding get() = mBinding!!

    lateinit var selectedStock: FinanceDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityStockInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedStock = StockListProvider.manageStocks[intent.getIntExtra("selectedStockPos", 0)!!]

        binding.stockTitleText.text = selectedStock.name
        binding.nowCost.text = selectedStock.now.toString()
        binding.stockArrowImage.setImageResource(when(selectedStock.risefall) {
            1 -> R.drawable.much_up_arrow
            2 -> R.drawable.little_up_arrow
            3 -> R.drawable.common_arrow
            4 -> R.drawable.much_down_arrow
            5 -> R.drawable.little_down_arrow
            else -> R.drawable.common_arrow
        })

        binding.characterImage.setImageResource(when(selectedStock.risefall) {
            1 -> R.drawable.much_up
            2 -> R.drawable.little_up
            3 -> R.drawable.common
            4 -> R.drawable.much_down
            5 -> R.drawable.little_down
            else -> R.drawable.common
        })

        binding.setWidgetButton.setOnClickListener {
            setWidgetStockPref(selectedStock.name)
        }
    }

    fun setWidgetStockPref(name: String) {
        PreferenceProvider.editor.putString("widgetStock", name)
        PreferenceProvider.editor.apply()
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, WidgetProvider::class.java))
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_layout)
        Toast.makeText(applicationContext, "위젯 주식으로 설정되었습니다.", Toast.LENGTH_LONG).show()
    }
}