package com.example.gramakhata.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gramakhata.R
import com.example.gramakhata.data.database.entity.LedgerTransaction
import com.example.gramakhata.data.database.entity.TransactionType
import com.example.gramakhata.utils.DateUtils

class TransactionAdapter :
    ListAdapter<LedgerTransaction, TransactionAdapter.Holder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(parent)

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    class Holder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)) {
        private val bar: View = itemView.findViewById(R.id.typeBar)
        private val iconBg: View = itemView.findViewById(R.id.iconBg)
        private val icon: ImageView = itemView.findViewById(R.id.typeIcon)
        private val title: TextView = itemView.findViewById(R.id.titleText)
        private val date: TextView = itemView.findViewById(R.id.dateText)
        private val amount: TextView = itemView.findViewById(R.id.amountText)

        fun bind(item: LedgerTransaction) {
            val isCredit = item.type == TransactionType.CREDIT
            val isPayment = item.type == TransactionType.PAYMENT
            
            val color = ContextCompat.getColor(itemView.context, if (isCredit) R.color.credit_red else R.color.payment_green)
            val bgColor = ContextCompat.getColor(itemView.context, if (isCredit) R.color.credit_red_light else R.color.primaryContainer)
            
            bar.setBackgroundColor(color)
            iconBg.backgroundTintList = android.content.res.ColorStateList.valueOf(bgColor)
            icon.setColorFilter(color)
            
            title.text = when (item.type) {
                TransactionType.CREDIT -> "Credit Given"
                TransactionType.PAYMENT -> "Payment Received"
                TransactionType.REMINDER -> "Reminder Sent"
            }
            date.text = listOfNotNull(DateUtils.formatDateTime(item.createdAt), item.note?.takeIf { it.isNotBlank() })
                .joinToString(" • ")
            
            val prefix = if (isCredit) "- " else if (isPayment) "+ " else ""
            amount.text = "$prefix${DateUtils.formatRupees(item.amount)}"
            amount.setTextColor(color)
            
            icon.setImageResource(when (item.type) {
                TransactionType.CREDIT -> R.drawable.ic_arrow_up
                TransactionType.PAYMENT -> R.drawable.ic_arrow_down
                TransactionType.REMINDER -> R.drawable.ic_send_outline
            })
        }
    }

    object Diff : DiffUtil.ItemCallback<LedgerTransaction>() {
        override fun areItemsTheSame(old: LedgerTransaction, new: LedgerTransaction) = old.transactionId == new.transactionId
        override fun areContentsTheSame(old: LedgerTransaction, new: LedgerTransaction) = old == new
    }
}
