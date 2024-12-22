package com.example.financeplaner.data.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeplaner.data.FinanceDatabase
import com.example.financeplaner.data.repository.FinanceRepository
import com.example.financeplaner.data.IncomeEntity
import kotlinx.coroutines.launch
import com.example.financeplaner.data.dao.IncomeDao
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
                .sortedByDescending { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(it.date) }
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
}


