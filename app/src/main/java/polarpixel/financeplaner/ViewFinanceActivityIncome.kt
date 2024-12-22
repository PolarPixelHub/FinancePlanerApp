package polarpixel.financeplaner

import android.os.Bundle
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import polarpixel.financeplaner.databinding.ActivityViewFinanceIncomeBinding
import polarpixel.financeplaner.data.ui.*
import polarpixel.financeplaner.data.IncomeAdapter

class ViewFinanceActivityIncome : AppCompatActivity() {

    private lateinit var binding: ActivityViewFinanceIncomeBinding
    private lateinit var incomeAdapter: IncomeAdapter
    private val financeViewModel: FinanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFinanceIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        incomeAdapter = IncomeAdapter(emptyList())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewFinanceActivityIncome)
            adapter = incomeAdapter
        }

        // Observe incomeList from ViewModel and update adapter
        financeViewModel.incomeList.observe(this, Observer { incomeList ->
            incomeAdapter.setData(incomeList)
        })

        // Load initial data
        financeViewModel.loadAllIncome()

        // Set the OnClickListener for the "View Finance" button
        binding.btnChooseDate.setOnClickListener {
            // Navigate to the ViewFinanceSetDateActivity
            val intentViewDated = Intent(this, ViewFinanceSetDateActivity::class.java)
            startActivity(intentViewDated)
        }

        // Set click listener for Cancel button
        binding.btnBack.setOnClickListener { finish() }
    }
}
