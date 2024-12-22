package polarpixel.financeplaner.data

import android.content.Context
import androidx.room.Room
import polarpixel.financeplaner.data.FinanceDatabase

object DatabaseProvider {
    private var instance: FinanceDatabase? = null

    fun getDatabase(context: Context): FinanceDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                FinanceDatabase::class.java,
                "income_database"
            ).build()
        }
        return instance!!
    }
}

