package polarpixel.financeplaner

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import polarpixel.financeplaner.data.ui.FinanceViewModel
import polarpixel.financeplaner.databinding.ActivityViewFinanceSetDateBinding
import polarpixel.financeplaner.data.IncomeAdapter

class ViewFinanceSetDateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewFinanceSetDateBinding
    private lateinit var incomeAdapter: IncomeAdapter
    private val financeViewModel: FinanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cashflow_all)

        binding = ActivityViewFinanceSetDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        incomeAdapter = IncomeAdapter(emptyList())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewFinanceSetDateActivity)
            adapter = incomeAdapter
        }

        // Observe the filtered income list and update the RecyclerView
        financeViewModel.filteredIncomeList.observe(this, Observer { incomeList ->
            incomeAdapter.setData(incomeList)
        })

        // Set up the button to trigger date range search
        binding.btnSearch.setOnClickListener {
            val startDate = binding.etStartDate.text.toString()
            val endDate = binding.etEndDate.text.toString()

            // Validate that dates are entered
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                // Call the ViewModel's function to fetch data
                financeViewModel.getIncomeBetweenDates(startDate, endDate)
            } else {
                // Show a message if dates are missing
                Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for Cancel button
        binding.btnBack.setOnClickListener { finish() }
    }
}
