package com.example.financeplaner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val type: String,
    val date: String // Use a date format that you prefer, or consider using a `Date` type with converters
)
