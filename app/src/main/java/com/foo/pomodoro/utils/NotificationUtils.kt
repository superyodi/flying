package com.foo.pomodoro.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.foo.pomodoro.MainActivity
import com.foo.pomodoro.R
import com.foo.pomodoro.TimerFragmentArgs
import com.foo.pomodoro.service.TimerService
import timber.log.Timber


fun provideBaseNotificationBuilder(
    app: Context,
    pendingIntent: PendingIntent,
): NotificationCompat.Builder = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
    .setAutoCancel(false)
    .setOngoing(true)
    .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
    .setContentTitle("뽀모도로 타이머")
    .setContentText("25:00")
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


fun buildTimeFragmentPendingIntentWithId(id: Int, context: Context): PendingIntent {

    val arg = TimerFragmentArgs(id).toBundle()

    return NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_pomodoro)
        .setDestination(R.id.timerFragment)
        .setArguments(arg)
        .createPendingIntent()
}



