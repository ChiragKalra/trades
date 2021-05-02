package com.bruhascended.trades.db

import android.icu.util.Calendar
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import kotlin.math.ceil

@Entity(tableName = "trades")
data class Trade (
    var name: String,

    val quantity: Float,
    val tradeFee: Float,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var buyDateTime: Long = 0,
    var buyPrice: Float = 0f,

    var sellDateTime: Long = 0,
    var sellPrice: Float = 0f,

    var inProgress: Boolean = true,

): Serializable {

    fun insertBuyPrice (price: Float) {
        buyPrice = price
        buyDateTime = Calendar.getInstance().timeInMillis
    }

    fun insertSellPrice (price: Float) {
        inProgress = false
        sellPrice = price
        sellDateTime = Calendar.getInstance().timeInMillis
    }

    fun getProfitPercentage() : Float {
        val fee = (sellPrice+buyPrice)/2 * tradeFee/100
        var p = 100 * (sellPrice - buyPrice - fee) / buyPrice
        p = ceil(p * 100) /100
        return p
    }

    fun wasProfitable() = getProfitPercentage() > 0

    fun getTotalInvested() = buyPrice * quantity

    fun getTotalSold() = (getTotalInvested() * (getProfitPercentage() + 100))/100

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Trade
        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
