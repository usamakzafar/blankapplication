package com.example.blankapplication.ui

import androidx.lifecycle.ViewModel
import com.example.blankapplication.ui.model.Event
import com.example.blankapplication.ui.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate

class MainViewModel: ViewModel() {

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState("", true, false))
    val state: StateFlow<UiState> = _state


    fun processEvent(event: Event) {
        when (event) {
            is Event.OnTextChange -> _state.getAndUpdate { it.copy(url = event.text) }
            Event.OnWebpageLoadSuccess -> _state.getAndUpdate { it.copy(loading = false) }
            Event.OnWebviewReturnedError -> _state.getAndUpdate { it.copy(loading = false, error = true) }
        }
    }
}