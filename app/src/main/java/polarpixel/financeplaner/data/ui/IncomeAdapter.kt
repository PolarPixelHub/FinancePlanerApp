package polarpixel.financeplaner.data.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import polarpixel.financeplaner.data.IncomeEntity
import polarpixel.financeplaner.R

class IncomeAdapter(
    private var incomeList: List<IncomeEntity>,
    private val clickListener: (IncomeEntity, Int) -> Unit // Add the clickListener parameter
) : RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {

    fun setData(newIncomeList: List<IncomeEntity>) {
        incomeList = newIncomeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_income, parent, false)
        return IncomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val income = incomeList[position]
        holder.bind(income)
        holder.itemView.setOnClickListener {
            clickListener(income, position) // Trigger the click listener
        }
    }

    override fun getItemCount(): Int = incomeList.size

    class IncomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(income: IncomeEntity) {
            tvAmount.text = income.amount.toString()
            tvType.text = income.type
            tvDate.text = income.date
        }
    }
}



