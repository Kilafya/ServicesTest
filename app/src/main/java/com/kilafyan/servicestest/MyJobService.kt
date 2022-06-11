package com.kilafyan.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService: JobService() {

    private val coroutinesScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("create")
    }

    override fun onDestroy() {
        coroutinesScope.cancel()
        log("destroy")
        super.onDestroy()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutinesScope.launch {
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent?.getIntExtra(PAGE, 0) ?: 0
                    for (i in 0..5) {
                        delay(1000)
                        log("Timer $i page $page")
                    }
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                jobFinished(params, false)
            }
            return true
        }
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    private fun log(message: String) {
        Log.d("JOB_SERVICE_TAG", "JobService: $message")
    }

    companion object {
        const val JOB_ID = 1001
        private const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}