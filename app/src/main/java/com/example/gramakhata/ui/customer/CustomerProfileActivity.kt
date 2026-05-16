package com.example.gramakhata.ui.customer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gramakhata.R
import com.example.gramakhata.data.database.entity.Customer
import com.example.gramakhata.data.database.entity.TransactionType
import com.example.gramakhata.ui.transaction.AddCreditDialog
import com.example.gramakhata.ui.transaction.AddPaymentDialog
import com.example.gramakhata.ui.transaction.TransactionAdapter
import com.example.gramakhata.ui.transaction.TransactionHistoryActivity
import com.example.gramakhata.utils.DateUtils
import com.example.gramakhata.utils.ReminderUtils
import com.example.gramakhata.viewmodel.CustomerDetailViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.io.File

class CustomerProfileActivity : AppCompatActivity(), AddCreditDialog.Listener, AddPaymentDialog.Listener {
    private val vm: CustomerDetailViewModel by viewModels()
    private var customerId = -1
    private var customer: Customer? = null
    private var netDue = 0.0
    private var shopName = "Grama-Khata"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)
        customerId = intent.getIntExtra("customerId", -1)
        
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        findViewById<ImageView>(R.id.moreButton).setOnClickListener { showCustomerMenu(it) }
        
        val adapter = TransactionAdapter()
        findViewById<RecyclerView>(R.id.recentList).apply { 
            layoutManager = LinearLayoutManager(this@CustomerProfileActivity)
            this.adapter = adapter 
        }
        
        val showCreditDialog = { AddCreditDialog().show(supportFragmentManager, "credit") }
        val showPaymentDialog = { AddPaymentDialog().show(supportFragmentManager, "payment") }
        findViewById<android.view.View>(R.id.profileCreditButton).setOnClickListener { showCreditDialog() }
        findViewById<android.view.View>(R.id.profilePaymentButton).setOnClickListener { showPaymentDialog() }
        findViewById<android.view.View>(R.id.profileReminderButton).setOnClickListener { sendReminder() }
        findViewById<android.view.View>(R.id.viewAllButton).setOnClickListener {
            startActivity(Intent(this, TransactionHistoryActivity::class.java).putExtra("customerId", customerId))
        }
        
        findViewById<TextView>(R.id.phoneText).setOnClickListener {
            customer?.phoneNumber?.let { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))) }
        }
        
        vm.shopkeeper.observe(this) { shopName = it?.shopName ?: "Grama-Khata" }
        vm.observeCustomer(customerId).observe(this) { bindCustomer(it) }
        vm.observeNetDue(customerId).observe(this) { bindDue(it) }
        vm.observeRecent(customerId).observe(this) {
            adapter.submitList(it.take(5))
        }
    }

    private fun showCustomerMenu(anchor: android.view.View) {
        PopupMenu(this, anchor).apply {
            menuInflater.inflate(R.menu.customer_menu, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuEdit -> {
                        startActivity(Intent(this@CustomerProfileActivity, EditCustomerActivity::class.java).putExtra("customerId", customerId))
                        true
                    }
                    R.id.menuHistory -> {
                        startActivity(Intent(this@CustomerProfileActivity, TransactionHistoryActivity::class.java).putExtra("customerId", customerId))
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    private fun bindCustomer(c: Customer?) {
        customer = c ?: return
        findViewById<TextView>(R.id.nameText).text = c.name
        findViewById<TextView>(R.id.phoneText).text = c.phoneNumber
        
        val photoView = findViewById<ImageView>(R.id.photo)
        if (c.photoPath != null) {
            val file = File(c.photoPath)
            photoView.load(file)
        } else {
            photoView.load(R.drawable.bg_circle_placeholder)
        }
    }

    private fun bindDue(value: Double) {
        netDue = value
        val isDue = value > 0
        val color = ContextCompat.getColor(this, if (isDue) R.color.credit_red else R.color.payment_green)
        
        findViewById<TextView>(R.id.netDueText).apply { 
            text = DateUtils.formatRupees(value)
            setTextColor(color) 
        }
        
        findViewById<TextView>(R.id.statusText).apply {
            text = when { 
                value > 0 -> "Credit Exceeded"
                value < 0 -> "Advance Paid" 
                else -> "Balance Clear" 
            }
            setTextColor(color)
        }
    }

    override fun onTransaction(type: TransactionType, amount: Double, note: String?, date: Long) {
        vm.addTransaction(customerId, type, amount, note, date) {
            val label = if (type == TransactionType.CREDIT) "credit added" else "payment recorded"
            Toast.makeText(this, "${DateUtils.formatRupees(amount)} $label", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendReminder() {
        val c = customer ?: return
        if (netDue <= 0) { 
            Toast.makeText(this, "No outstanding due for ${c.name}", Toast.LENGTH_SHORT).show()
            return 
        }
        val text = ReminderUtils.message(c.name, shopName, netDue)
        vm.addTransaction(customerId, TransactionType.REMINDER, 0.0, "Reminder sent", System.currentTimeMillis())
        ReminderUtils.send(this, c.phoneNumber, text)
    }
}
