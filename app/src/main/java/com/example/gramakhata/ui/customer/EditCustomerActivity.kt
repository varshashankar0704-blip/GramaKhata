package com.example.gramakhata.ui.customer

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import coil.load
import com.example.gramakhata.R
import com.example.gramakhata.data.database.entity.Customer
import com.example.gramakhata.utils.ImageUtils
import com.example.gramakhata.utils.SecurityUtils
import com.example.gramakhata.viewmodel.CustomerDetailViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File

class EditCustomerActivity : AppCompatActivity() {
    private val vm: CustomerDetailViewModel by viewModels()
    private var current: Customer? = null
    private var photoPath: String? = null
    private var cameraUri: Uri? = null
    private val picker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { savePhoto(it) }
    }
    private val camera = registerForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) cameraUri?.let { savePhoto(it) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_form)
        val id = intent.getIntExtra("customerId", -1)
        findViewById<MaterialToolbar>(R.id.toolbar).apply { title = "Edit Customer"; setNavigationIcon(android.R.drawable.ic_menu_revert); setNavigationOnClickListener { finish() } }
        findViewById<Button>(R.id.saveButton).text = "Update Customer"
        findViewById<Button>(R.id.deleteButton).visibility = android.view.View.VISIBLE
        findViewById<Button>(R.id.photoButton).setOnClickListener { choosePhoto() }
        findViewById<Button>(R.id.saveButton).setOnClickListener { save() }
        findViewById<Button>(R.id.deleteButton).setOnClickListener { confirmDelete() }
        vm.observeCustomer(id).observe(this) { customer ->
            if (customer == null) return@observe
            current = customer; photoPath = customer.photoPath
            findViewById<TextInputEditText>(R.id.nameInput).setText(customer.name)
            findViewById<TextInputEditText>(R.id.phoneInput).setText(customer.phoneNumber)
            customer.photoPath?.let { findViewById<ImageView>(R.id.customerPhoto).load(File(it)) }
        }
    }
    private fun choosePhoto() = AlertDialog.Builder(this).setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
        if (which == 0) {
            val file = File(cacheDir.resolve("camera").apply { mkdirs() }, "customer_${System.currentTimeMillis()}.jpg")
            cameraUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
            camera.launch(cameraUri)
        } else picker.launch("image/*")
    }.show()
    private fun savePhoto(uri: Uri) {
        photoPath = ImageUtils.saveCustomerPhoto(this, uri)
        photoPath?.let { findViewById<ImageView>(R.id.customerPhoto).load(File(it)) }
    }
    private fun save() {
        val c = current ?: return
        val name = findViewById<TextInputEditText>(R.id.nameInput).text.toString().trim()
        val phone = findViewById<TextInputEditText>(R.id.phoneInput).text.toString().trim()
        findViewById<TextInputLayout>(R.id.nameLayout).error = null; findViewById<TextInputLayout>(R.id.phoneLayout).error = null
        if (name.isBlank()) { findViewById<TextInputLayout>(R.id.nameLayout).error = "Customer name is required"; return }
        if (!SecurityUtils.isMobile(phone)) { findViewById<TextInputLayout>(R.id.phoneLayout).error = "Phone must be 10 digits"; return }
        vm.updateCustomer(c.copy(name = name, phoneNumber = phone, photoPath = photoPath)) { ok, duplicate ->
            if (ok) { Toast.makeText(this, "Customer updated", Toast.LENGTH_SHORT).show(); finish() }
            if (duplicate) findViewById<TextInputLayout>(R.id.phoneLayout).error = "Phone number already exists"
        }
    }
    private fun confirmDelete() = current?.let { customer ->
        AlertDialog.Builder(this).setTitle("Delete customer?").setMessage("All transaction history will also be deleted. This cannot be undone.").setPositiveButton("Delete") { _, _ -> vm.deleteCustomer(customer) { finish() } }.setNegativeButton("Cancel", null).show()
    } ?: Unit
}
