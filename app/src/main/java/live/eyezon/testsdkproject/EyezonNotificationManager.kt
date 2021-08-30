package live.eyezon.testsdkproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.gson.Gson

class EyezonNotificationManager(private val context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "eyezon_sdk_notification"
        const val NOTIFICATION_CHANNEL_NAME = "SDK Notifications"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "SDK Channel description"

        const val ACTION_OPEN_DIALOG = "action.open.dialog"
        const val ACTION_OPEN_APP = "action.open.app"

        const val REQUEST_CODE = 955
    }

    fun sendNotification(pushType: String, content: String) {
        when (pushType) {
            "MESSAGE" -> showNewMessageInRequestNotification(content)
            else -> createAndShowPush(pushType, content, pushType, "")
        }
    }

    private fun showNewMessageInRequestNotification(content: String) {
        try {
            val message = Gson().fromJson(content, NewMessagePush::class.java)
            val contentText = "New ${message.type} message"
            createAndShowPush(
                "Unread message", contentText, "MESSAGE", message.dialogId
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createAndShowPush(
        contentTitle: String, contentText: String, type: String, dialogId: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationChannel.description = NOTIFICATION_CHANNEL_DESCRIPTION
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(
            context, NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)

        val pendingIntent = when (type) {
            "MESSAGE" ->
                PushBroadcastReceiver.getIntent(context, ACTION_OPEN_DIALOG, dialogId)
            else -> PushBroadcastReceiver.getIntent(context, ACTION_OPEN_APP)
        }

        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(REQUEST_CODE, notificationBuilder.build())
    }
}
