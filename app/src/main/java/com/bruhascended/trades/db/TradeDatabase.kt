package com.bruhascended.trades.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Trade::class], version = 1, exportSchema = false)
abstract class TradeDatabase: RoomDatabase() {
    abstract fun manager(): TradeDao
}