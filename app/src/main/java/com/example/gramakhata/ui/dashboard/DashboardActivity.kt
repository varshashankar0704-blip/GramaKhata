package com.example.gramakhata.ui.dashboard

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.example.gramakhata.R
import com.example.gramakhata.data.database.dao.CustomerBalance
import com.example.gramakhata.ui.customer.AddCustomerActivity
import com.example.gramakhata.ui.customer.CustomerProfileActivity
import com.example.gramakhata.ui.report.DailyReportActivity
import com.example.gramakhata.ui.settings.SettingsActivity
import com.example.gramakhata.utils.DateUtils
import com.example.gramakhata.viewmodel.DashboardViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.File

class DashboardActivity : AppCompatActivity() {
    private val vm: DashboardViewModel by viewModels()
    private var allCustomers = emptyList<CustomerBalance>()
    private lateinit var customerList: LinearLayout
    private lateinit var customerCountText: TextView
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val toolbar = findViewById<TextView>(R.id.toolbar)
        customerList = findViewById(R.id.customerList)
        customerCountText = findViewById(R.id.customerCountText)
        emptyText = findViewById(R.id.emptyText)
        findViewById<View>(R.id.addCustomerFab).setOnClickListener { startActivity(Intent(this, AddCustomerActivity::class.java)) }
        findViewById<View>(R.id.navHome).setOnClickListener { renderCustomers() }
        findViewById<View>(R.id.navReports).setOnClickListener { startActivity(Intent(this, DailyReportActivity::class.java)) }
        findViewById<View>(R.id.navSettings).setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
        findViewById<TextInputEditText>(R.id.searchInput).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { vm.query.value = s?.toString().orEmpty() }
            override fun afterTextChanged(s: Editable?) = Unit
        })
        vm.shopkeeper.observe(this) {
            val userName = it?.shopName?.takeIf { name -> name.isNotBlank() } ?: "User"
            toolbar.text = "Welcome, $userName"
        }
        vm.todayCollection.observe(this) { findViewById<TextView>(R.id.todayCollectionText).text = DateUtils.formatRupees(it) }
        vm.customerBalances.observe(this) { list ->
            allCustomers = list
            renderCustomers()
            val total = list.filter { it.netDue > 0 }.sumOf { it.netDue }
            findViewById<TextView>(R.id.totalOutstandingText).text = DateUtils.formatRupees(total)
        }
    }

    private fun renderCustomers() {
        val list = allCustomers
        customerList.removeAllViews()
        list.forEach { customerList.addView(createCustomerRow(it)) }
        emptyText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        customerCountText.text = "Customers (${allCustomers.size})"
        setNavSelected(R.id.navHome, true)
    }

    private fun createCustomerRow(item: CustomerBalance): View {
        val row = LayoutInflater.from(this).inflate(R.layout.item_customer, customerList, false)
        row.findViewById<TextView>(R.id.nameText).text = item.name
        row.findViewById<TextView>(R.id.phoneText).text = item.phoneNumber
        val hasDue = item.netDue > 0
        row.findViewById<TextView>(R.id.dueText).apply {
            text = DateUtils.formatRupees(item.netDue)
            setTextColor(ContextCompat.getColor(this@DashboardActivity, if (hasDue) R.color.credit_red else R.color.payment_green))
        }
        row.findViewById<TextView>(R.id.dueLabelText).apply {
            text = if (hasDue) "DUE" else "PAID"
            setTextColor(ContextCompat.getColor(this@DashboardActivity, if (hasDue) R.color.onSurfaceVariant else R.color.payment_green))
        }
        row.findViewById<ImageView>(R.id.photo).let { photo ->
            if (item.photoPath != null) photo.load(File(item.photoPath)) else photo.setImageResource(R.drawable.bg_circle_placeholder)
        }
        row.setOnClickListener {
            startActivity(Intent(this, CustomerProfileActivity::class.java).putExtra("customerId", item.customerId))
        }
        return row
    }

    private fun setNavSelected(navId: Int, selected: Boolean) {
        val nav = findViewById<LinearLayout>(navId)
        val color = ContextCompat.getColor(this, if (selected) R.color.primary else R.color.onSurfaceVariant)
        (nav.getChildAt(0) as? ImageView)?.setColorFilter(color)
        (nav.getChildAt(1) as? TextView)?.apply {
            setTextColor(color)
            typeface = Typeface.defaultFromStyle(if (selected) Typeface.BOLD else Typeface.NORMAL)
        }
    }
}
