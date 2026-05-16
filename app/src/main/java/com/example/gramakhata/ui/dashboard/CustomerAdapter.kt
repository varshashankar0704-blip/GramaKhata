package com.example.gramakhata.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gramakhata.R
import com.example.gramakhata.data.database.dao.CustomerBalance
import com.example.gramakhata.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File

class CustomerAdapter(private val onClick: (CustomerBalance) -> Unit) :
    ListAdapter<CustomerBalance, CustomerAdapter.Holder>(Diff) {
    private val monthYearFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(parent, onClick)

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position), monthYearFormat)

    class Holder(parent: ViewGroup, private val onClick: (CustomerBalance) -> Unit) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)) {
        private val photo: ImageView = itemView.findViewById(R.id.photo)
        private val name: TextView = itemView.findViewById(R.id.nameText)
        private val memberSince: TextView = itemView.findViewById(R.id.memberSinceText)
        private val phone: TextView = itemView.findViewById(R.id.phoneText)
        private val lastTransaction: TextView = itemView.findViewById(R.id.lastTransactionText)
        private val due: TextView = itemView.findViewById(R.id.dueText)
        private val dueLabel: TextView = itemView.findViewById(R.id.dueLabelText)

        fun bind(item: CustomerBalance, monthYearFormat: SimpleDateFormat) {
            name.text = item.name
            val hasDue = item.netDue > 0
            phone.text = item.phoneNumber
            memberSince.text = "Today, ${monthYearFormat.format(Date(item.createdAt))}"
            lastTransaction.text = DateUtils.formatDate(item.createdAt)
            due.text = DateUtils.formatRupees(item.netDue)
            due.setTextColor(ContextCompat.getColor(itemView.context, if (hasDue) R.color.credit_red else R.color.payment_green))
            dueLabel.text = if (hasDue) "DUE" else "PAID"
            dueLabel.setTextColor(ContextCompat.getColor(itemView.context, if (hasDue) R.color.onSurfaceVariant else R.color.payment_green))
            if (item.photoPath != null) {
                photo.load(File(item.photoPath))
            } else {
                photo.setImageResource(R.drawable.bg_circle_placeholder)
            }
            itemView.setOnClickListener { onClick(item) }
        }
    }

    object Diff : DiffUtil.ItemCallback<CustomerBalance>() {
        override fun areItemsTheSame(old: CustomerBalance, new: CustomerBalance) = old.customerId == new.customerId
        override fun areContentsTheSame(old: CustomerBalance, new: CustomerBalance) = old == new
    }
}
