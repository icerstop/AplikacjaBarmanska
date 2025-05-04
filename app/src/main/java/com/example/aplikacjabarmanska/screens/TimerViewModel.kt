package com.example.aplikacjabarmanska.screens

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacjabarmanska.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private var timerService: TimerService? = null
    private val _isServiceBound = MutableStateFlow(false)

    // Stan minutnika dla aktualnie wybranego koktajlu
    private val _cocktailId = MutableStateFlow<Int?>(null)
    private val _formattedTime = MutableStateFlow("00:00")
    private val _isRunning = MutableStateFlow(false)

    // Ekspozycja stanów na zewnątrz
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    // Połączenie z serwisem
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            _isServiceBound.value = true

            // Po podłączeniu serwisu, aktualizuj stan jeśli jest ustawiony cocktailId
            _cocktailId.value?.let { subscribeToTimer(it) }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerService = null
            _isServiceBound.value = false
        }
    }

    init {
        // Bind service
        val intent = Intent(getApplication(), TimerService::class.java)
        application.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        application.startService(intent)
    }

    fun setCocktailId(id: Int) {
        if (_cocktailId.value != id) {
            _cocktailId.value = id
            if (_isServiceBound.value) {
                subscribeToTimer(id)
            }
        }
    }

    private fun subscribeToTimer(cocktailId: Int) {
        timerService?.let { service ->
            val timer = service.getOrCreateTimer(cocktailId)

            viewModelScope.launch {
                timer.remainingTime.collect { timeMs ->
                    _formattedTime.value = formatTime(timeMs)
                }
            }

            viewModelScope.launch {
                timer.isRunning.collect { running ->
                    _isRunning.value = running
                }
            }
        }
    }

    private fun formatTime(timeMs: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMs) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun startTimer(seconds: Long) {
        _cocktailId.value?.let { id ->
            timerService?.getOrCreateTimer(id)?.start(seconds)
        }
    }

    fun stopTimer() {
        _cocktailId.value?.let { id ->
            timerService?.getOrCreateTimer(id)?.stop()
        }
    }

    fun resetTimer() {
        _cocktailId.value?.let { id ->
            timerService?.getOrCreateTimer(id)?.reset()
        }
    }

    override fun onCleared() {
        getApplication<Application>().unbindService(connection)
        super.onCleared()
    }
}