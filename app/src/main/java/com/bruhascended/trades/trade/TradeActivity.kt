package com.bruhascended.trades.trade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bruhascended.trades.R
import com.bruhascended.trades.databinding.ActivityTradeBinding
import com.bruhascended.trades.db.Trade
import com.bruhascended.trades.newtrade.NewTradeActivity
import com.bruhascended.trades.newtrade.ProfitPercentageAdaptor
import com.bruhascended.trades.trades.MainActivity
import com.bruhascended.trades.trades.MainActivity.Companion.setupToolbar
import com.bruhascended.trades.util.getFullDateTime

class TradeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTradeBinding
    private var mId: Long = 0
    private lateinit var mTrade: Trade

    private fun TextView.updateProfit() {
        if (mTrade.wasProfitable()) {
            text = '+' + mTrade.getProfitPercentage().toString() + '%'
            setTextColor(getColor(R.color.green))
        } else {
            text = mTrade.getProfitPercentage().toString() + '%'
            setTextColor(getColor(R.color.red))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_trade)
        mId = intent.getLongExtra("id", 0)

        setupToolbar(binding.toolbar, getString(R.string.trade))

        mTrade = MainActivity.mdb?.manager()?.findByNumber(mId) ?: return
        binding.tableLayout.layoutManager = LinearLayoutManager(this)
        binding.tableLayout.adapter = ProfitPercentageAdaptor(
                NewTradeActivity.percentages, mTrade.buyPrice, mTrade.tradeFee
        )

        binding.apply {
            name.text = mTrade.name
            value.text = mTrade.quantity.toString()
            net.text = "$" + (mTrade.buyPrice * mTrade.quantity).toString()
            buyPrice.text = mTrade.buyPrice.toString()
            buyDate.text = getFullDateTime(mTrade.buyDateTime)

            if (mTrade.inProgress) {
                fiat.isVisible = true
                actionSold.isVisible = true
                sellPrice.isVisible = false
                sellDateLayout.isVisible = false

                fiat.addTextChangedListener {
                    mTrade.insertSellPrice(
                            it.toString().toFloatOrNull()
                                    ?: return@addTextChangedListener
                    )
                    increase.updateProfit()
                    if (it.isNullOrBlank() || it.toString().toFloat() <= 0) {
                        actionSold.setOnClickListener { }
                    } else {
                        actionSold.setOnClickListener {
                            MainActivity.mdb?.manager()?.insert(mTrade)
                            startActivity(
                                    Intent(
                                            this@TradeActivity,
                                            TradeActivity::class.java
                                    ).apply {
                                        putExtra("id", mId)
                                    }
                            )
                            finish()
                        }
                    }
                }
            } else {
                fiat.isVisible = false
                actionSold.isVisible = false
                sellPrice.isVisible = true
                sellDateLayout.isVisible = true

                sellPrice.text = mTrade.sellPrice.toString()
                sellDate.text = getFullDateTime(mTrade.sellDateTime)

                increase.updateProfit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_trade, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_delete) {
            MainActivity.mdb?.manager()?.delete(mTrade)
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}