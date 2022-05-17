package com.example.mystock.ui.view

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.Toast
import com.example.mystock.R
import com.example.mystock.data.PreferenceProvider
import com.example.mystock.data.StockListProvider
import com.example.mystock.data.api.ApiProvider
import com.example.mystock.data.dto.FinanceDto
import com.example.mystock.data.dto.StockDto
import com.example.mystock.databinding.ActivityMainBinding
import com.example.mystock.ui.WidgetProvider
import com.example.mystock.ui.adapter.StockListAdapter
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.min

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val binding get() = mBinding!!

    private val financeApi = ApiProvider.provideFinanceApi()
    private var financeCall: Call<FinanceDto>? = null

    private val stockListAdapter by lazy {
        StockListAdapter(this)
    }

    private lateinit var stockList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PreferenceProvider.prefs = getSharedPreferences("myStock", MODE_PRIVATE)
        val prefs_data = PreferenceProvider.prefs.getString("manageStock", "")
        if (!prefs_data.equals("")) {
            val jsonArray = JSONArray(prefs_data)
            var stockList = mutableListOf<String>()
            for(i in 0 until jsonArray.length()) {
                stockList.add(jsonArray[i].toString())
            }
            initManageStock(stockList, )
        }

        stockList = binding.stockList
        stockList.adapter = stockListAdapter

        stockList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(binding.root.context, StockInfoActivity::class.java)
            intent.putExtra("selectedStockPos", position)
            PreferenceProvider.context = applicationContext
            startActivity(intent)
        }

        binding.searchButton.setOnClickListener {
            if (StockListProvider.stockCodes.containsKey(binding.searchEdit.text.toString())) {
                addManageStock(binding.searchEdit.text.toString(),
                    StockListProvider.stockCodes.get(binding.searchEdit.text.toString()).toString(), true)
            } else {
                Toast.makeText(applicationContext, "존재하지 않는 주식입니다.", Toast.LENGTH_LONG).show()
            }
            binding.searchEdit.text.clear()
            binding.searchEdit.clearFocus()
            clearEditKeyboard()
        }
    }

    fun initManageStock(stockList: MutableList<String>) {
        for(stockName in stockList) {
            addManageStock(stockName, StockListProvider.stockCodes[stockName].toString(), false)
        }
    }

    fun addManageStock(name: String, code: String, move: Boolean) {
        for(i in 0 until StockListProvider.manageStocks.size) {
            if (StockListProvider.manageStocks[i].name == name) {
                StockListProvider.manageStocks.removeAt(i)
                break
            }
        }

        financeCall = financeApi.getFinance(code)
        financeCall?.enqueue(object: Callback<FinanceDto> {
            override fun onResponse(call: Call<FinanceDto>, response: Response<FinanceDto>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    body.name = name
                    StockListProvider.manageStocks = (mutableListOf(body) + StockListProvider.manageStocks).toMutableList()
                    stockListAdapter.notifyDataSetChanged()
                    setManagePref()

                    if (move) {
                        val intent = Intent(binding.root.context, StockInfoActivity::class.java)
                        intent.putExtra("selectedStockPos", 0)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<FinanceDto>, t: Throwable) {
            }

        })
    }

    fun clearEditKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEdit.windowToken, 0)
    }

    fun setManagePref() {
        var jsonArray = JSONArray()
        for(i in 0 until min(20, StockListProvider.manageStocks.size)) {
            jsonArray.put(StockListProvider.manageStocks[i].name)
        }
        if (!StockListProvider.manageStocks.isEmpty()) {
            PreferenceProvider.editor.putString("manageStock", jsonArray.toString())
        } else {
            PreferenceProvider.editor.putString("manageStock", "")
        }
        PreferenceProvider.editor.apply()
    }
}