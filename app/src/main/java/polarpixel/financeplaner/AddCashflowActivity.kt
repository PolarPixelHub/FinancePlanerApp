package polarpixel.financeplaner

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import polarpixel.financeplaner.data.ui.FinanceViewModelFactory
import polarpixel.financeplaner.data.IncomeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import polarpixel.financeplaner.databinding.ActivityAddCashflowBinding
import polarpixel.financeplaner.data.ui.FinanceViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddCashflowActivity : AppCompatActivity() {

    // Flag to track if the button was clicked
    private var isIncomeButtonClicked = false

    // ViewBinding for the layout
    private lateinit var binding: ActivityAddCashflowBinding
    private lateinit var financeViewModel: FinanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cashflow)

        // Initialize the ViewModel with the required factory
        val factory = FinanceViewModelFactory(application)
        financeViewModel = ViewModelProvider(this, factory)[FinanceViewModel::class.java]

        // Observe income types and update the spinner
        financeViewModel.incomeTypes.observe(this) { types ->
            updateEntryTypeSpinner(types)
        }

        binding = ActivityAddCashflowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonsStyle()
        //set up UI listeners
        setupListeners()
    }

    private fun setupButtonsStyle() {
        binding.btnIncome.setBackgroundColor(R.style.CustomButtonStyle)
        binding.btnTrack.setBackgroundColor(R.style.CustomButtonStyle)
        binding.btnBack.setBackgroundColor(R.style.CustomButtonStyle)
    }

    private fun setupListeners() {
        binding.btnAdd.setOnClickListener { showCreateEntryDialog() }   // Set the OnClickListener for the "+" Button
        binding.btnIncome.setOnClickListener { toggleIncomeView() }
        binding.tvDate.setOnClickListener { showDatePicker() }          // Set the OnClickListener for the Date TextView to show a DatePicker
        binding.btnTrack.setOnClickListener { trackCashflow() }         // Track button click listener
        binding.btnBack.setOnClickListener { finish() }               // Set click listener for Cancel button
    }

    // Toggle visibility for income-related fields
    private fun toggleIncomeView() {
        if (!isIncomeButtonClicked) {
            showIncomeFields()
        } else {
            hideIncomeFields()
        }
    }

    // Show income input fields
    private fun showIncomeFields() {
        binding.btnIncome.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.apply {
            incomeLayer.visibility = View.VISIBLE
        }
        isIncomeButtonClicked = true
    }

    // Hide income input fields
    private fun hideIncomeFields() {
        binding.btnIncome.setBackgroundColor(R.style.CustomButtonStyle)
        binding.apply {
            incomeLayer.visibility = View.GONE
        }
        isIncomeButtonClicked = false
    }

    // Track button logic
    private fun trackCashflow() {
        val entryType = binding.spinnerEntryTypes.selectedItem.toString()
        val amount = binding.etAmount.text.toString().toDoubleOrNull()  // Added null check for safety
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
                    financeViewModel.addIncome(
                        polarpixel.financeplaner.data.IncomeEntity(
                            type = entryType,
                            amount = 0.0,
                            date = ""
                        )
                    )
                    financeViewModel.loadIncomeTypes()
                }
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        }

        dialogBuilder.create().show()
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

    // Update the spinner with new data
    private fun updateEntryTypeSpinner(types: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerEntryTypes.adapter = adapter
    }
}
