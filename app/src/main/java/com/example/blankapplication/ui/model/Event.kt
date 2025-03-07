package com.example.blankapplication.ui.model

sealed class Event {
    data class OnTextChange(val text: String): Event()
    data object OnWebviewReturnedError: Event()
    data object OnWebpageLoadSuccess: Event()
}