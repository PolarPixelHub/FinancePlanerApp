package polarpixel.financeplaner.data.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import polarpixel.financeplaner.data.ui.FinanceViewModel

class FinanceViewModelFactory(
    private val application: Application // Pass the application context here
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            // Pass Application context to the ViewModel
            return FinanceViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

