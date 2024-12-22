package polarpixel.financeplaner.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import polarpixel.financeplaner.R

class IncomeAdapter(private var incomeList: List<IncomeEntity>) :
    RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {

    fun setData(newIncomeList: List<IncomeEntity>) {
        val diffCallback = IncomeDiffCallback(incomeList, newIncomeList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        incomeList = newIncomeList
        diffResult.dispatchUpdatesTo(this)
    }

    // ViewHolder to hold the views for each item
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_income, parent, false)
        return IncomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val income = incomeList[position]
        holder.bind(income)
    }

    override fun getItemCount() = incomeList.size

    class IncomeDiffCallback(
        private val oldList: List<IncomeEntity>,
        private val newList: List<IncomeEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

