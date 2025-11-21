package com.example.qualitywash.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.qualitywash.ui.Data.HistoryRepository
import com.example.qualitywash.ui.Data.Purchase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel(private val userId: String) : ViewModel() {

    private val _historyState = MutableStateFlow<List<Purchase>>(emptyList())
    val historyState: StateFlow<List<Purchase>> = _historyState

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _historyState.value = HistoryRepository.getUserHistory(userId)
    }
}