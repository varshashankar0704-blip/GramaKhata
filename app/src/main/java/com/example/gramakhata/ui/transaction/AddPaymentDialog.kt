package com.example.gramakhata.ui.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.gramakhata.R
import com.example.gramakhata.data.database.entity.TransactionType
import com.example.gramakhata.utils.DateUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

class AddPaymentDialog : DialogFragment() {
    interface Listener { fun onTransaction(type: TransactionType, amount: Double, note: String?, date: Long) }
    private var selectedDate = System.currentTimeMillis()
    override fun onCreateDialog(state: Bundle?) = androidx.appcompat.app.AlertDialog.Builder(requireContext()).setTitle("Payment Received").setView(createContent()).setPositiveButton("Add Payment", null).setNegativeButton("Cancel", null).create()
    private fun createContent(): View {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_transaction, null, false)
        val dateButton = view.findViewById<Button>(R.id.dateButton)
        fun refreshDate() { dateButton.text = DateUtils.formatDate(selectedDate) }
        refreshDate()
        dateButton.setOnClickListener {
            val c = Calendar.getInstance().apply { timeInMillis = selectedDate }
            DatePickerDialog(requireContext(), { _, y, m, d -> selectedDate = Calendar.getInstance().apply { set(y, m, d) }.timeInMillis; refreshDate() }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        val d = dialog as androidx.appcompat.app.AlertDialog
        d.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val amountInput = d.findViewById<TextInputEditText>(R.id.amountInput) ?: return@setOnClickListener
            val amount = amountInput.text.toString().toDoubleOrNull()
            if (amount == null || amount <= 0) { d.findViewById<TextInputLayout>(R.id.amountLayout)?.error = "Amount must be greater than 0"; return@setOnClickListener }
            (activity as? Listener)?.onTransaction(TransactionType.PAYMENT, amount, d.findViewById<TextInputEditText>(R.id.noteInput)?.text.toString().ifBlank { null }, selectedDate)
            dismiss()
        }
    }
}
