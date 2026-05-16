package com.example.gramakhata.ui.transaction

import android.app.AlertDialog
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gramakhata.R
import com.example.gramakhata.data.database.entity.LedgerTransaction
import com.example.gramakhata.data.database.entity.TransactionType
import com.example.gramakhata.utils.DateUtils
import com.example.gramakhata.viewmodel.CustomerDetailViewModel
import com.google.android.material.appbar.MaterialToolbar

class TransactionHistoryActivity : AppCompatActivity() {
    private val vm: CustomerDetailViewModel by viewModels()
    private var all = emptyList<LedgerTransaction>()
    private val adapter = TransactionAdapter()
    private var filter: TransactionType? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_transaction_history)
        val id = intent.getIntExtra("customerId", -1)
        findViewById<MaterialToolbar>(R.id.toolbar).apply { setNavigationIcon(android.R.drawable.ic_menu_revert); setNavigationOnClickListener { finish() } }
        val list = findViewById<RecyclerView>(R.id.transactionList)
        list.layoutManager = LinearLayoutManager(this); list.adapter = adapter
        vm.observeCustomer(id).observe(this) { findViewById<MaterialToolbar>(R.id.toolbar).title = "${it?.name ?: "Customer"} History" }
        vm.observeTransactions(id).observe(this) { all = it; publish() }
        vm.observeTotals(id).observe(this) {
            val due = it.credit - it.payment
            findViewById<TextView>(R.id.summaryText).text = "Total Credit: ${DateUtils.formatRupees(it.credit)}\nTotal Payment: ${DateUtils.formatRupees(it.payment)}\nNet Due: ${DateUtils.formatRupees(due)}"
        }
        findViewById<RadioGroup>(R.id.filterGroup).setOnCheckedChangeListener { _, checked ->
            filter = when (checked) { R.id.filterCredit -> TransactionType.CREDIT; R.id.filterPayment -> TransactionType.PAYMENT; else -> null }
            publish()
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val position = vh.absoluteAdapterPosition
                val item = adapter.currentList[position]
                AlertDialog.Builder(this@TransactionHistoryActivity).setTitle("Delete this transaction?").setMessage("Net balance will update.").setPositiveButton("Delete") { _, _ -> vm.deleteTransaction(item) }.setNegativeButton("Cancel") { _, _ -> adapter.notifyItemChanged(position) }.show()
            }
        }).attachToRecyclerView(list)
    }
    private fun publish() = adapter.submitList(if (filter == null) all else all.filter { it.type == filter })
}
