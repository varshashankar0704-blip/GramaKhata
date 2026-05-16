package com.example.gramakhata.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object ReminderUtils {
    fun message(customerName: String, shopName: String, amount: Double): String =
        "Namaskara $customerName,\nYour due at $shopName is ${DateUtils.formatRupees(amount)}.\nPlease pay soon.\nThank you."

    fun send(activity: Activity, phone: String, text: String) {
        val whatsapp = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        try {
            activity.startActivity(whatsapp)
        } catch (_: ActivityNotFoundException) {
            val sms = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phone")
                putExtra("sms_body", text)
            }
            try {
                activity.startActivity(sms)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(activity, "No WhatsApp or SMS app found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
