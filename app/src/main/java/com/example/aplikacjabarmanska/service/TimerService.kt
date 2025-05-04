package com.example.aplikacjabarmanska.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerService : Service() {
    private val binder = TimerBinder()
    private val timers = mutableMapOf<Int, CocktailTimer>()

    // Klasa wewnętrzna reprezentująca timer dla konkretnego koktajlu
    inner class CocktailTimer(private val cocktailId: Int) {
        private var countDownTimer: CountDownTimer? = null
        private val _remainingTime = MutableStateFlow(0L)
        val remainingTime: StateFlow<Long> = _remainingTime.asStateFlow()

        private val _isRunning = MutableStateFlow(false)
        val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

        private var totalTime: Long = 0L

        fun start(seconds: Long) {
            stop()
            totalTime = seconds * 1000
            _remainingTime.value = totalTime

            countDownTimer = object : CountDownTimer(totalTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _remainingTime.value = millisUntilFinished
                }

                override fun onFinish() {
                    _remainingTime.value = 0
                    _isRunning.value = false
                }
            }

            countDownTimer?.start()
            _isRunning.value = true
        }

        fun stop() {
            countDownTimer?.cancel()
            _isRunning.value = false
        }

        fun reset() {
            stop()
            _remainingTime.value = 0
        }
    }

    // Metody dla aktywności do interakcji z serwisem
    fun getOrCreateTimer(cocktailId: Int): CocktailTimer {
        return timers.getOrPut(cocktailId) { CocktailTimer(cocktailId) }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onDestroy() {
        // Zatrzymaj wszystkie timery przed zniszczeniem serwisu
        timers.values.forEach { it.stop() }
        super.onDestroy()
    }
}