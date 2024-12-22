package polarpixel.financeplaner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import polarpixel.financeplaner.databinding.ActivityCashflowAllBinding

class CashflowAllActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCashflowAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashflow_all)

        binding = ActivityCashflowAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonsStyle()
        setupListeners()

    }

    private fun setupButtonsStyle() {
        binding.btnIncome.setBackgroundColor(R.style.CustomButtonStyle)
        binding.btnSavings.setBackgroundColor(R.style.CustomButtonStyle)
        binding.btnExpenses.setBackgroundColor(R.style.CustomButtonStyle)
    }

    private fun setupListeners() {
        binding.btnIncome.setOnClickListener { startActivity<ViewFinanceIncomeActivity>() }
        //binding.btnSavings.setOnClickListener { startActivity<>() }
        //binding.btnExpenses.setOnClickListener { startActivity<>() }
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}