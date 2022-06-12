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
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyJobIntentService: JobIntentService() {

    override fun onCreate() {
        super.onCreate()
        log("create")
    }

    override fun onHandleWork(intent: Intent) {
        val page = intent.getIntExtra(PAGE, 0)
        for (i in 0..10) {
            Thread.sleep(1000)
            log("Timer $i page $page")
        }
    }

    private fun log(message: String) {
        Log.d("JOB_INTENT_SERVICE_TAG", "MyJobIntentService: $message")
    }


    override fun onDestroy() {
        log("destroy")
        super.onDestroy()
    }

    companion object {
        private const val PAGE = "page"
        private const val JOB_ID = 1234

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}