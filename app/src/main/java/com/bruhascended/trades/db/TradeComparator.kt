package com.bruhascended.trades.db

import androidx.recyclerview.widget.DiffUtil


object TradeComparator : DiffUtil.ItemCallback<Trade>() {
    override fun areItemsTheSame(oldItem: Trade, newItem: Trade) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Trade, newItem: Trade) =
        oldItem == newItem
}
