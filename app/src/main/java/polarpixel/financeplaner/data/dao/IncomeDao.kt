package polarpixel.financeplaner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import polarpixel.financeplaner.data.IncomeEntity

@Dao
interface IncomeDao {
    @Insert
    suspend fun insertIncome(income: IncomeEntity)

    @Query("SELECT * FROM income WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getIncomeBetweenDates(startDate: String, endDate: String): List<IncomeEntity>

    @Query("SELECT * FROM income ORDER BY date DESC")
    suspend fun getAllIncome(): List<IncomeEntity>

    // Fetch distinct types from IncomeEntity
    @Query("SELECT DISTINCT type FROM income")
    suspend fun getAllIncomeTypes(): List<String>

    // Update an existing income record
    @Update
    suspend fun updateIncome(income: IncomeEntity)

    @Query("DELETE FROM income WHERE type = :type")
    fun deleteIncomeByType(type: String)

    @Query("SELECT * FROM income WHERE date = :date")
    fun getIncomeByDate(date: String): List<IncomeEntity>

    @Delete
    fun deleteIncome(incomeEntity: IncomeEntity)
}
