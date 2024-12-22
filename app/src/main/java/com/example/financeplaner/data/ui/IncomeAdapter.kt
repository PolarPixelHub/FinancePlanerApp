package com.example.financeplaner.data.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financeplaner.R
import com.example.financeplaner.data.IncomeEntity

class IncomeAdapter(private var incomeList: List<IncomeEntity>) :
    RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {

    // Update the list of items shown in the RecyclerView
    fun setData(newIncomeList: List<IncomeEntity>) {
        incomeList = newIncomeList
        notifyDataSetChanged() // Refresh RecyclerView data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_income, parent, false)
        return IncomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val income = incomeList[position]
        holder.bind(income)
    }

    override fun getItemCount(): Int = incomeList.size

    class IncomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        // Bind each IncomeEntity's data to the UI
        fun bind(income: IncomeEntity) {
            tvAmount.text = income.amount.toString()
            tvType.text = income.type
            tvDate.text = income.date
        }
    }
}
