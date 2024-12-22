package polarpixel.financeplaner.data.ui

import android.app.Application
import android.net.ParseException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import polarpixel.financeplaner.data.repository.FinanceRepository
import polarpixel.financeplaner.data.IncomeEntity
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Locale

class FinanceViewModel(application: Application) : AndroidViewModel(application) {

    // Create repository instance
    private val repository = FinanceRepository(application)

    // Expose income data as LiveData for observation
    private val _incomeList = MutableLiveData<List<IncomeEntity>>()
    val incomeList: LiveData<List<IncomeEntity>> get() = _incomeList

    // LiveData to hold filtered income data
    private val _filteredIncomeList = MutableLiveData<List<IncomeEntity>>()
    val filteredIncomeList: LiveData<List<IncomeEntity>> get() = _filteredIncomeList

    // LiveData for distinct income types
    private val _incomeTypes = MutableLiveData<List<String>>()
    val incomeTypes: LiveData<List<String>> get() = _incomeTypes

    // Function to add income
    fun addIncome(income: IncomeEntity) {
        viewModelScope.launch {
            repository.addIncome(income)
        }
    }

    // Method to fetch income between dates (for filtering in future)
    fun getIncomeBetweenDates(startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val filteredData = repository.getIncomeBetweenDates(startDate, endDate)
            _incomeList.postValue(filteredData)
        }
    }

    // Function to load all income records
    fun loadAllIncome() {
        viewModelScope.launch(Dispatchers.IO) {
            val incomeData = repository.getAllIncome()
                .sortedByDescending { incomeEntity ->
                    // Handle empty or invalid dates
                    val dateString = incomeEntity.date
                    if (dateString.isNullOrEmpty()) {
                        Log.e("FinanceViewModel", "Empty or null date found: $incomeEntity")
                        null
                    } else {
                        try {
                            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(dateString)
                        } catch (e: ParseException) {
                            Log.e("FinanceViewModel", "Error parsing date: $dateString", e)
                            null
                        }
                    }
                }
                .filterNotNull() // Exclude entries with null dates
            _incomeList.postValue(incomeData)
        }
    }


    // Function to load distinct income types
    fun loadIncomeTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            val types = repository.getAllIncomeTypes()
            _incomeTypes.postValue(types)
        }
    }

    fun deleteIncomeByType(type: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteIncomeByType(type)
        }
    }

    fun getIncomeByDate(date: String): List<IncomeEntity> {
        return repository.getIncomeByDate(date)
    }

    fun deleteIncome(incomeEntity: IncomeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteIncome(incomeEntity)
        }
    }
}


