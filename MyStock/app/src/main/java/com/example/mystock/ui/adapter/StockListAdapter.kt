package com.example.mystock.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.mystock.R
import com.example.mystock.data.StockListProvider
import com.example.mystock.data.dto.FinanceDto
import com.example.mystock.databinding.ItemStockBinding

class StockListAdapter (val context: Context) : BaseAdapter() {
    override fun getCount(): Int = StockListProvider.manageStocks.size

    override fun getItem(position: Int): FinanceDto = StockListProvider.manageStocks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(context))

        val item = StockListProvider.manageStocks[position]
        binding.stockTitleText.text = item.name
        binding.nowCost.text = item.now.toString()
        binding.varCost.text = item.diff.toString()
        binding.varPercent.text = item.rate.toString()
        binding.stockArrowImage.setImageResource(when(item.risefall) {
            1 -> R.drawable.much_up_arrow
            2 -> R.drawable.little_up_arrow
            3 -> R.drawable.common_arrow
            4 -> R.drawable.much_down_arrow
            5 -> R.drawable.little_down_arrow
            else -> R.drawable.common_arrow
        })

        return binding.root
    }
}