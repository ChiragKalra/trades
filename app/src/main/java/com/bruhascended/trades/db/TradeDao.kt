package com.bruhascended.trades.db

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

 @Dao
interface TradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trade: Trade)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(trades: Array<Trade>)

    @Delete
    fun delete(trade: Trade)

    @Query("DELETE FROM trades")
    fun nukeTable()

    @Query("SELECT * FROM trades WHERE id LIKE :id")
    fun findByNumber(id: Long): Trade?

    @Query("SELECT * FROM trades")
    fun loadAllSync(): Array<Trade>

    @Query("SELECT * FROM trades order by buyDateTime DESC")
    fun loadAllPaged(): PagingSource<Int, Trade>

    @Query("SELECT * FROM trades WHERE LOWER(name) LIKE :key order by buyDateTime DESC")
    fun searchPaged(key: String): PagingSource<Int, Trade>

    @RawQuery
    fun findByQuery(query: SupportSQLiteQuery): Array<Trade>

}
