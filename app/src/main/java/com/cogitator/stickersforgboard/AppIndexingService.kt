package com.cogitator.stickersforgboard

import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService

/**
 * @author Ankit Kumar on 26/11/2018
 */
class AppIndexingService : JobIntentService() {

    // Job-ID must be unique across your whole app.
    private val UNIQUE_JOB_ID = 42

    fun enqueueWork(context: Context) {
        JobIntentService.enqueueWork(context, AppIndexingService::class.java, UNIQUE_JOB_ID, Intent())
    }

    override fun onHandleWork(p0: Intent) {

        StickerIndexingUtil.setStickers(applicationContext, FirebaseAppIndex.getInstance())
    }

}