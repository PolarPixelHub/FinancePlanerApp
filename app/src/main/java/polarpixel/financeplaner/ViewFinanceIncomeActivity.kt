package polarpixel.financeplaner

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import polarpixel.financeplaner.data.IncomeAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import polarpixel.financeplaner.data.IncomeEntity
import polarpixel.financeplaner.data.ui.FinanceViewModel
import polarpixel.financeplaner.databinding.ActivityViewFinanceIncomeBinding

class ViewFinanceIncomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewFinanceIncomeBinding
    private lateinit var incomeAdapter: IncomeAdapter
    private val financeViewModel: FinanceViewModel by viewModels()
    private lateinit var incomeEntryView: RecyclerView
    private lateinit var incomeEntryAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFinanceIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        incomeAdapter = IncomeAdapter(emptyList())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewFinanceIncomeActivity)
            adapter = incomeAdapter
        }

        // Observe incomeList from ViewModel and update adapter
        financeViewModel.incomeList.observe(this, Observer { incomeList ->
            incomeAdapter.setData(incomeList)
        })

        // Load initial data
        financeViewModel.loadAllIncome()

        setupButtonsStyle()
        setupListeners()

        incomeEntryView = findViewById(R.id.recyclerView)
    }

    private fun setupButtonsStyle() {
        binding.btnBack.setBackgroundColor(R.style.CustomButtonStyle)
        binding.btnChooseDate.setBackgroundColor(R.style.CustomButtonStyle)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnChooseDate.setOnClickListener {
            val intentViewDated = Intent(this, ViewFinanceSetDateActivity::class.java)
            startActivity(intentViewDated)
        }
    }

    private fun showIncomeOptions(income: IncomeEntity, position: Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Manage Income")
            .setMessage("What would you like to do with this income entry?")
            .setPositiveButton("Edit") { _, _ ->
                Toast.makeText(this, "Edit functionality not implemented yet.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Delete") { _, _ ->
                financeViewModel.deleteIncome(income)
                Toast.makeText(this, "Income deleted successfully.", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Cancel", null)
            .create()

        dialog.show()
    }
}
