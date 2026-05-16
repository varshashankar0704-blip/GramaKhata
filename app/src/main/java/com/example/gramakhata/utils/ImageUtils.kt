package com.example.gramakhata.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun saveCustomerPhoto(context: Context, source: Uri): String? = runCatching {
        val input = context.contentResolver.openInputStream(source) ?: return null
        val bitmap = BitmapFactory.decodeStream(input) ?: return null
        val size = minOf(bitmap.width, bitmap.height)
        val square = Bitmap.createBitmap(bitmap, (bitmap.width - size) / 2, (bitmap.height - size) / 2, size, size)
        val dir = File(context.filesDir, "customer_photos").apply { mkdirs() }
        val file = File(dir, "customer_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { square.compress(Bitmap.CompressFormat.JPEG, 82, it) }
        file.absolutePath
    }.getOrNull()
}
