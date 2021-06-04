package com.foo.pomodoro.utils

// https://jizard.tistory.com/

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.foo.pomodoro.MainActivity
import com.foo.pomodoro.R
import com.foo.pomodoro.service.TimerService



// Notification ID.

private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // TODO: Step 2.0 add style
    val timerImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.juyeon
    )


    // TODO: stop timer action
    val stopIntent = Intent(applicationContext, TimerService::class.java)
    val stopPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        stopIntent,
        FLAGS)

    // TODO: cancel timer action
    val cancelIntent = Intent(applicationContext, TimerService::class.java)
    val cancelPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        cancelIntent,
        FLAGS)

    // TODO: start timer action
    val restartIntent = Intent(applicationContext, TimerService::class.java)
    val restartPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        restartIntent,
        FLAGS)


    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.timer_notification_channel_id)
    )

        // TODO: Step 1.8 use the new 'breakfast' notification channel

        // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)

        // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // TODO: Step 2.1 add style to builder
        .setLargeIcon(timerImage)
        .setContentText("00:00")

        .addAction(
            R.drawable.ic_baseline_access_alarm_24,
            applicationContext.getString(R.string.timer_stop),
            stopPendingIntent
        )
        .addAction(
            R.drawable.ic_baseline_access_alarm_24,
            applicationContext.getString(R.string.timer_cancel),
            cancelPendingIntent
        )
        .addAction(
            R.drawable.ic_baseline_access_alarm_24,
            applicationContext.getString(R.string.timer_restart),
            restartPendingIntent
        )

        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}


fun provideMainActivityPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getActivity(
    app,
    0,
    Intent(app, MainActivity::class.java).also {
        it.action = ACTION_SHOW_MAIN_ACTIVITY
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)



fun provideCancelActionPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getService(
    app,
    1,
    Intent(app, TimerService::class.java).also {
        it.action = ACTION_CANCEL
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)



fun provideResumeActionPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getService(
    app,
    2,
    Intent(app, TimerService::class.java).also {
        it.action = ACTION_RESUME
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)


fun providePauseActionPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getService(
    app,
    3,
    Intent(app, TimerService::class.java).also {
        it.action = ACTION_PAUSE
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)


fun provideBaseNotificationBuilder(
    app: Context,
    pendingIntent: PendingIntent
): NotificationCompat.Builder = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
    .setAutoCancel(false)
    .setOngoing(true)
    .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
    .setContentTitle("INTime")
    .setContentText("00:00:00")
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