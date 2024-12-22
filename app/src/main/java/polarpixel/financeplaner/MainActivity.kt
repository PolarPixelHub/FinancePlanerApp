package polarpixel.financeplaner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import polarpixel.financeplaner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonsStyle()
        setupListeners()

    }

    private fun setupButtonsStyle() {
        binding.btnViewCashflow.setBackgroundColor(R.style.CustomButtonStyle)
        binding.btnAddCashflow.setBackgroundColor(R.style.CustomButtonStyle)
    }

    private fun setupListeners() {
        binding.btnViewCashflow.setOnClickListener { startActivity<CashflowAllActivity>() }
        binding.btnAddCashflow.setOnClickListener  { startActivity<AddCashflowActivity>() }
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}



