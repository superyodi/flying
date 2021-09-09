package com.yodi.flying.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.yodi.flying.MainActivity
import com.yodi.flying.R

import com.yodi.flying.features.timer.TimerFragmentArgs
import com.yodi.flying.utils.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.yodi.flying.utils.Constants.Companion.NOTIFICATION_CHANNEL_NAME
import timber.log.Timber


fun provideBaseNotificationBuilder(
    app: Context,
    pendingIntent: PendingIntent,
): NotificationCompat.Builder = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
    .setAutoCancel(false)
    .setOngoing(true)
    .setSmallIcon(R.drawable.ic_flying)
    .setContentTitle("Flying")
    .setContentText("곧 비행기가 이륙합니다.🛫")
    .setContentIntent(pendingIntent)

fun provideNotificationManager(
    app: Context
): NotificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager


@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(notificationManager: NotificationManager) {
    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_LOW
    )
    notificationManager.createNotificationChannel(channel)
}


fun buildTimeFragmentPendingIntentWithId(id: Long, context: Context): PendingIntent {

    val arg = TimerFragmentArgs(id).toBundle()

    return NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_pomodoro)
        .setDestination(R.id.timerFragment)
        .setArguments(arg)
        .setComponentName(MainActivity::class.java)
        .createPendingIntent()
}



