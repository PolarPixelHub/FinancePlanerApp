package com.example.financeplaner

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_view_finances).setOnClickListener {
            startActivity<ViewFinanceActivity>()
        }

        findViewById<Button>(R.id.btn_add_cashflow).setOnClickListener {
            startActivity<AddCashflowActivity>()
        }
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}



