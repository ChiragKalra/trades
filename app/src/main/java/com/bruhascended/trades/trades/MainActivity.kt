package com.bruhascended.trades.trades

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bruhascended.trades.R
import com.bruhascended.trades.databinding.ActivityMainBinding
import com.bruhascended.trades.db.TradeDatabase
import com.bruhascended.trades.newtrade.NewTradeActivity
import com.bruhascended.trades.trade.TradeActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        var mdb: TradeDatabase? = null

        fun AppCompatActivity.setupToolbar(
                toolbar: Toolbar, mTitle: String? = null, home : Boolean = true
        ) {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(home)
                setDisplayShowHomeEnabled(home)
                title = mTitle ?: return
            }
        }

    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdaptor: TradeAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mdb = Room.databaseBuilder(
            this, TradeDatabase::class.java, "trades"
        ).allowMainThreadQueries().build()

        setSupportActionBar(binding.toolbar)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, NewTradeActivity::class.java))
        }

        mAdaptor = TradeAdaptor(
                getColor(R.color.red),
                getColor(R.color.green)
        ).apply {
            setOnItemClickListener {
                startActivity(Intent(this@MainActivity, TradeActivity::class.java).apply {
                    putExtra("id", it)
                })
            }
        }

        binding.tradeRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdaptor
        }

        val flow = Pager(
            PagingConfig(
                pageSize = 15,
                initialLoadSize = 15,
                prefetchDistance = 60,
                maxSize = 180,
            )
        ) {
            mdb!!.manager().loadAllPaged()
        }.flow

        lifecycleScope.launch {
            flow.cachedIn(lifecycleScope).collectLatest {
                mAdaptor.submitData(it)
            }
        }
    }
}