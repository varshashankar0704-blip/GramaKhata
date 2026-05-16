package com.example.gramakhata.ui.report

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gramakhata.R
import com.example.gramakhata.data.database.dao.Totals
import com.example.gramakhata.data.database.entity.LedgerTransaction
import com.example.gramakhata.ui.transaction.TransactionAdapter
import com.example.gramakhata.utils.DateUtils
import com.example.gramakhata.viewmodel.ReportViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.util.Calendar

class DailyReportActivity : AppCompatActivity() {
    private val vm: ReportViewModel by viewModels()
    private val adapter = TransactionAdapter()
    private var day = System.currentTimeMillis()
    private var txSource: LiveData<List<LedgerTransaction>>? = null
    private var totalsSource: LiveData<Totals>? = null
    private var transactions = emptyList<LedgerTransaction>()
    private var totals = Totals(0.0, 0.0)
    private var shopName = "Grama-Khata"

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_daily_report)
        findViewById<MaterialToolbar>(R.id.toolbar).apply { setNavigationIcon(android.R.drawable.ic_menu_revert); setNavigationOnClickListener { finish() } }
        findViewById<RecyclerView>(R.id.reportList).apply { layoutManager = LinearLayoutManager(this@DailyReportActivity); adapter = this@DailyReportActivity.adapter }
        findViewById<android.widget.Button>(R.id.prevButton).setOnClickListener { shift(-1) }
        findViewById<android.widget.Button>(R.id.nextButton).setOnClickListener { shift(1) }
        findViewById<android.widget.Button>(R.id.shareButton).setOnClickListener { shareReport() }
        vm.shopkeeper.observe(this) { shopName = it?.shopName ?: "Grama-Khata" }
        bindDay()
    }
    private fun shift(days: Int) {
        day = Calendar.getInstance().apply { timeInMillis = day; add(Calendar.DAY_OF_YEAR, days) }.timeInMillis
        bindDay()
    }
    private fun bindDay() {
        findViewById<TextView>(R.id.dateText).text = DateUtils.formatDate(day)
        txSource?.removeObservers(this); totalsSource?.removeObservers(this)
        txSource = vm.transactionsFor(day).also { it.observe(this) { list -> transactions = list; adapter.submitList(list) } }
        totalsSource = vm.totalsFor(day).also { it.observe(this) { t -> totals = t; findViewById<TextView>(R.id.summaryText).text = summaryText() } }
    }
    private fun summaryText() = "Total Credit: ${DateUtils.formatRupees(totals.credit)}\nTotal Payment: ${DateUtils.formatRupees(totals.payment)}\nNet Change: ${DateUtils.formatRupees(totals.credit - totals.payment)}"
    private fun shareReport() {
        val lines = buildString {
            appendLine("$shopName Daily Collection Report")
            appendLine(DateUtils.formatDate(day))
            appendLine(summaryText())
            appendLine()
            transactions.forEach { appendLine("${it.type}: ${DateUtils.formatRupees(it.amount)} - ${it.note.orEmpty()} (${DateUtils.formatDateTime(it.createdAt)})") }
        }
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, lines) }, "Share report"))
    }
}
