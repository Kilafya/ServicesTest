package com.kilafyan.servicestest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegroundService: Service() {

    private val coroutinesScope = CoroutineScope(Dispatchers.Main)

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    private val notificationBuilder by lazy {
        createNotificationBuilder()
    }

    var onProgressChanged: ((Int) -> Unit)? = null

        override fun onCreate() {
            super.onCreate()
            log("create")
            createNotificationChannel()
            startForeground(NOTIFICATION_ID, notificationBuilder.build())
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            coroutinesScope.launch {
                for (i in 0..100 step 5) {
                    delay(1000)
                    val notification = notificationBuilder
                        .setProgress(100, i, false)
                        .build()
                    log("Timer $i")
                    notificationManager.notify(NOTIFICATION_ID, notification)
                    onProgressChanged?.invoke(i)
                }
                stopSelf()
            }
            return START_NOT_STICKY
        }

        private fun log(message: String) {
            Log.d("FOREGROUND_SERVICE_TAG", "MyForegroundService: $message")
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        private fun createNotificationBuilder() = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setProgress(100, 0, false)
            .setOnlyAlertOnce(true)


        override fun onBind(intent: Intent?): IBinder {
            return LocalBinder()
        }

        override fun onDestroy() {
            coroutinesScope.cancel()
            log("destroy")
            super.onDestroy()
        }

    inner class LocalBinder: Binder() {

        fun getService() = this@MyForegroundService
    }

        companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context) = Intent(context, MyForegroundService::class.java)
    }
}