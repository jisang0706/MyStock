package com.example.mystock.data

import com.example.mystock.data.dto.FinanceDto

object StockListProvider {

    var stockCodes: Map<String, String> = mutableMapOf(Pair("삼성전자", "005930"),
        Pair("SK하이닉스", "000660"), Pair("기아", "000270"))

    var manageStocks: MutableList<FinanceDto> = mutableListOf()
}