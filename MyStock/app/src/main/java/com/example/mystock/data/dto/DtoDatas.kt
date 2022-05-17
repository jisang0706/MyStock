package com.example.mystock.data.dto

data class FinanceDto (
    val marketSum: Int,
    val risefall: Int,
    val diff: Int,
    val rate: Double,
    val high: Int,
    val low: Int,
    val quant: Int,
    val amount: Int,
    val per: Double,
    val eps: Double,
    val pbr: Double,
    val now: Int
        ) {
    lateinit var name: String
}

class StockDto {
    var name: String = ""
    var code: String = ""

    override fun toString(): String {
        return String.format("StockDto{name=%s\', code=%s\'}", name, code)
    }

    fun fromString(str: String) {

    }
}