package com.bruhascended.trades.newtrade

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bruhascended.trades.R
import com.bruhascended.trades.databinding.ActivityNewTradeBinding
import com.bruhascended.trades.db.Trade
import com.bruhascended.trades.db.TradeDatabase
import com.bruhascended.trades.trades.MainActivity
import com.bruhascended.trades.trades.MainActivity.Companion.setupToolbar

class NewTradeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTradeBinding
    private lateinit var inputMethodManager: InputMethodManager

    companion object {
        private val percentages = Array(11) { it*5 }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_trade)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setupToolbar(binding.toolbar, getString(R.string.add_new_trade))

        binding.tableLayout.layoutManager = LinearLayoutManager(this)

        binding.fiat.addTextChangedListener {
            val value = binding.fiat.text.toString().toFloatOrNull()
            val fee = binding.fee.text.toString().toFloatOrNull()
            if (fee == null || value == null) return@addTextChangedListener
            binding.tableLayout.adapter = ProfitPercentageAdaptor(percentages, value, fee)
        }

        binding.fee.addTextChangedListener {
            val value = binding.fiat.text.toString().toFloatOrNull()
            val fee = binding.fee.text.toString().toFloatOrNull()
            if (fee == null || value == null) return@addTextChangedListener
            binding.tableLayout.adapter = ProfitPercentageAdaptor(percentages, value, fee)
        }
        binding.value.postDelayed(
            {
                binding.value.requestFocus()
                inputMethodManager.toggleSoftInputFromWindow(
                    binding.value.applicationWindowToken,
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }, 200
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_new_buy, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_done) {
            val value = binding.value.text.toString().toFloatOrNull()
            val price = binding.fiat.text.toString().toFloatOrNull()
            val fee = binding.fee.text.toString().toFloatOrNull()
            val name = binding.name.text.toString()
            if (price == null || fee == null || value == null || name.isBlank())
            {
                false
            } else {
                if (MainActivity.mdb == null) {
                    MainActivity.mdb = Room.databaseBuilder(
                        this, TradeDatabase::class.java, "trades"
                    ).allowMainThreadQueries().build()
                }
                val trade = Trade(name, value, fee)
                trade.insertBuyPrice(price)
                MainActivity.mdb!!.manager().insert(trade)
                finish()
                true
            }
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}