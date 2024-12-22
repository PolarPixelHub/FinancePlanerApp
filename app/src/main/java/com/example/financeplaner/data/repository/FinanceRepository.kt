package com.example.financeplaner.data.repository

import android.content.Context
import com.example.financeplaner.data.*

class FinanceRepository(context: Context) {
    private val incomeDao = DatabaseProvider.getDatabase(context).incomeDao()

    // Insert a new income record
    suspend fun addIncome(income: IncomeEntity) {
        incomeDao.insertIncome(income)
    }

    // Get income records between two dates
    suspend fun getIncomeBetweenDates(startDate: String, endDate: String): List<IncomeEntity> {
        return incomeDao.getIncomeBetweenDates(startDate, endDate)
    }

    // Update income record
    suspend fun updateIncome(income: IncomeEntity) {
        incomeDao.updateIncome(income)
    }

    suspend fun getAllIncome(): List<IncomeEntity> {
        return incomeDao.getAllIncome()
    }

    suspend fun getAllIncomeTypes(): List<String> {
        return incomeDao.getAllIncomeTypes()
    }
}