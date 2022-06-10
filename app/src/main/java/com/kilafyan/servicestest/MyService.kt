package com.kilafyan.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService: Service() {

    private val coroutinesScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("create")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val start = intent?.getIntExtra(KEY_START, 0) ?: 0
        coroutinesScope.launch {
            for (i in start..start + 100) {
                delay(1000)
                log("Timer $i")
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        coroutinesScope.cancel()
        log("destroy")
        super.onDestroy()
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyService: $message")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        private const val KEY_START = "start"

        fun newIntent(context: Context) =
            Intent(context, MyService::class.java).apply {
                putExtra(KEY_START, 25)
            }
    }
}