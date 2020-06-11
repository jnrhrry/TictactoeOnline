package com.januar.tictactoe

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat


class Notification(){
    val notifyTag = "New Invitation"
    fun Notify(context: android.content.Context, message: String, number:Int){
        val intent = Intent(context, LoginActivity::class.java)
        val builder=NotificationCompat.Builder(context)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle("Hai! Temanmu mengajak kamu bermain!")
            .setContentText(message)
            .setNumber(number)
            .setSmallIcon(R.drawable.logotictactoe)
            .setContentIntent(PendingIntent.getActivity(context,0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT))
            .setAutoCancel(true)
        val nm=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR){
        nm.notify(notifyTag,0,builder.build())

    }else {
            nm.notify(notifyTag.hashCode(),builder.build())
        }
}

}