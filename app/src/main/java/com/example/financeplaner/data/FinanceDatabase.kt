package com.example.financeplaner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financeplaner.data.IncomeEntity
import com.example.financeplaner.data.dao.IncomeDao

@Database(entities = [IncomeEntity::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao

    companion object {
        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getDatabase(context: Context): FinanceDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                FinanceDatabase::class.java,
                "finance_database"
            ).build().also { INSTANCE = it }
        }
    }
}
