package com.example.financeplaner

import android.app.DatePickerDialog
import android.os.Bundle
import com.example.financeplaner.data.dao.IncomeDao
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.financeplaner.data.IncomeEntity
import com.example.financeplaner.data.ui.FinanceViewModel
import com.example.financeplaner.data.ui.FinanceViewModelFactory
import com.example.financeplaner.databinding.ActivityAddCashflowBinding
import java.text.SimpleDateFormat
import androidx.core.content.ContextCompat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCashflowActivity : AppCompatActivity() {

    // Flag to track if the button was clicked
    private var isIncomeButtonClicked = false

    // List to store entry types created by the user
    private val entryTypes = mutableListOf<String>()

    // ViewBinding for the layout
    private lateinit var binding: ActivityAddCashflowBinding
    private lateinit var financeViewModel: FinanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCashflowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe income types and update the spinner
        financeViewModel.incomeTypes.observe(this) { types ->
            updateEntryTypeSpinner(types)
        }
        // Fetch distinct types from the database
        CoroutineScope(Dispatchers.IO).launch {
            financeViewModel.incomeTypes // Trigger LiveData observation
        }
        // Load entry types from the database
        financeViewModel.loadIncomeTypes()

        // Initialize the ViewModel with the required repository
        val factory = FinanceViewModelFactory(application)
        financeViewModel = ViewModelProvider(this, factory)[FinanceViewModel::class.java]

        // Set today's date as the default date in the TextView
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(today.time)

        // Set the OnClickListener for the Income Button
        binding.btnIncome.setOnClickListener { toggleIncomeView() }

        // Track button click listener
        binding.btnTrack.setOnClickListener { trackCashflow() }

        // Set click listener for Cancel button
        binding.btnCancel.setOnClickListener { finish() }

        // Set the OnClickListener for the "+" Button
        binding.btnAdd.setOnClickListener { showCreateEntryDialog() }

        // Set the OnClickListener for the Date TextView to show a DatePicker
        binding.tvDate.setOnClickListener { showDatePicker() }
    }

    // Toggle visibility for income-related fields
    private fun toggleIncomeView() {
        if (!isIncomeButtonClicked) {
            showIncomeFields()
        } else {
            hideIncomeFields()
            Toast.makeText(this, "Income button is disabled", Toast.LENGTH_SHORT).show()
        }
    }

    // Show income input fields
    private fun showIncomeFields() {
        binding.btnIncome.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        binding.apply {
            btnAdd.visibility = View.VISIBLE
            spinnerEntryTypes.visibility = View.VISIBLE
            etAmount.visibility = View.VISIBLE
            tvDate.visibility = View.VISIBLE
            tvTypeLabel.visibility = View.VISIBLE
            amountText.visibility = View.VISIBLE
            tvDateLabel.visibility = View.VISIBLE
        }
        isIncomeButtonClicked = true
    }

    // Hide income input fields
    private fun hideIncomeFields() {
        binding.btnIncome.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_purple))
        binding.apply {
            btnAdd.visibility = View.GONE
            spinnerEntryTypes.visibility = View.GONE
            etAmount.visibility = View.GONE
            tvDate.visibility = View.GONE
            tvTypeLabel.visibility = View.GONE
            amountText.visibility = View.GONE
            tvDateLabel.visibility = View.GONE
        }
        isIncomeButtonClicked = false
    }

    // Track button logic
    private fun trackCashflow() {
        val entryType = binding.spinnerEntryTypes.selectedItem.toString()
        val amount = binding.etAmount.text.toString().toDouble()
        val date = binding.tvDate.text.toString()

        if (amount != null && date.isNotEmpty()) {
            val income = IncomeEntity(amount = amount, type = entryType, date = date)
            // Insert the data in the database asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                financeViewModel.addIncome(income)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddCashflowActivity, "Income tracked!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
        }
    }

    // Show the DatePicker dialog
    private fun showDatePicker() {
        val todayDate = Calendar.getInstance()
        val year = todayDate.get(Calendar.YEAR)
        val month = todayDate.get(Calendar.MONTH)
        val day = todayDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // Show the dialog to create a new entry type
    private fun showCreateEntryDialog() {
        val inputEditText = EditText(this).apply {
            hint = "Enter new entry type"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
        }

        val dialogBuilder = AlertDialog.Builder(this).apply {
            setTitle("Create New Entry Type")
            setMessage("Please enter the type of entry you want to create.")
            setView(inputEditText)
            setPositiveButton("OK") { _, _ ->
                val entryType = inputEditText.text.toString().trim()
                if (entryType.isNotEmpty()) {
                    // Add to ViewModel's income types
                    financeViewModel.addIncome(IncomeEntity(type = entryType, amount = 0.0, date = ""))
                    financeViewModel.loadIncomeTypes()
                }
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        }

        dialogBuilder.create().show()
    }

    private fun updateEntryTypeSpinner(types: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerEntryTypes.adapter = adapter
    }
}

