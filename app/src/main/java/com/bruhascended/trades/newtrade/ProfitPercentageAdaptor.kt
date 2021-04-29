package com.bruhascended.trades.newtrade

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bruhascended.trades.R

class ProfitPercentageAdaptor(
    private val percentages: Array<Int>,
    private val initValue: Float,
    private val fee: Float
):
    RecyclerView.Adapter<ProfitPercentageAdaptor.ProfitPercentageViewHolder>()
{

    class ProfitPercentageViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val percentageText: TextView = root.findViewById(R.id.increase)
        val valueText: TextView = root.findViewById(R.id.value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfitPercentageViewHolder {
        return ProfitPercentageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_profit_percentage,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = percentages.size

    override fun onBindViewHolder(holder: ProfitPercentageViewHolder, position: Int) {
        holder.percentageText.text = '+' + percentages[position].toString() + '%'
        var value = initValue * (1 + (fee + percentages[position])/100)
        holder.valueText.text = value.toString()
    }
}