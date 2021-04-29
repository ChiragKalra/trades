package com.bruhascended.trades.trades

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bruhascended.trades.R
import com.bruhascended.trades.db.Trade
import com.bruhascended.trades.db.TradeComparator

class TradeAdaptor (
        private val red: Int,
        private val green: Int
): PagingDataAdapter<Trade, TradeAdaptor.TradeViewHolder> (TradeComparator) {
    private var mOnItemClickListener: ((id: Long) -> Unit)? = null

    class TradeViewHolder(
        val root: View
    ) : RecyclerView.ViewHolder(root) {
        val percentageText: TextView = root.findViewById(R.id.increase)
        val valueText: TextView = root.findViewById(R.id.value)
        val buyPriceText: TextView = root.findViewById(R.id.buy_price)
        val sellPriceText: TextView = root.findViewById(R.id.sell_price)
        val nameText: TextView = root.findViewById(R.id.name)
        val arrowImage: ImageView = root.findViewById(R.id.arrow)
        val net: TextView = root.findViewById(R.id.net)
    }

    fun setOnItemClickListener (listener: ((id: Long) -> Unit)?) {
        mOnItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeViewHolder {
        return TradeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_trade,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: TradeViewHolder, position: Int) {
        val trade = getItem(position) ?: return
        holder.apply {
            if (!trade.inProgress) {
                arrowImage.isVisible = true
                sellPriceText.isVisible = true
                percentageText.isVisible = true

                if (trade.wasProfitable()) {
                    percentageText.text = '+' + trade.getProfitPercentage().toString() + '%'
                    percentageText.setTextColor(green)
                } else {
                    percentageText.text = trade.getProfitPercentage().toString() + '%'
                    percentageText.setTextColor(red)
                }
                sellPriceText.text = "Rs " + trade.sellPrice.toString()
            } else {
                arrowImage.isVisible = false
                sellPriceText.isVisible = false
                percentageText.isVisible = false
            }
            buyPriceText.text = "Rs " + trade.buyPrice.toString()
            net.text = "Rs " + (trade.buyPrice * trade.quantity).toString()
            valueText.text = trade.quantity.toString()
            nameText.text = trade.name

            if (mOnItemClickListener != null) {
                root.setOnClickListener {
                    mOnItemClickListener?.invoke(trade.id)
                }
            }
        }
    }
}