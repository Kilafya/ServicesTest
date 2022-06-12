package com.kilafyan.servicestest

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyIntentService2: IntentService(NAME) {

    override fun onCreate() {
        super.onCreate()
        log("create")
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val page = intent?.getIntExtra(PAGE, 0) ?: 0
        for (i in 0..10) {
            Thread.sleep(1000)
            log("Timer $i page $page")
        }
    }

    private fun log(message: String) {
        Log.d("INTENT_SERVICE_TAG", "MyIntentService2: $message")
    }


    override fun onDestroy() {
        log("destroy")
        super.onDestroy()
    }

    companion object {
        private const val NAME = "MyIntentService2"
        private const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}