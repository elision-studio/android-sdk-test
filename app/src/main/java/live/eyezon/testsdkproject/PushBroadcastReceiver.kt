package live.eyezon.testsdkproject

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import live.eyezon.testsdkproject.EyezonNotificationManager.Companion.ACTION_OPEN_APP
import live.eyezon.testsdkproject.EyezonNotificationManager.Companion.ACTION_OPEN_DIALOG

class PushBroadcastReceiver : BroadcastReceiver() {

    companion object {

        private const val DIALOG_ID =
            "com.witheyezon.eyezonbusiness.firebase.notification.dialog.id"

        fun getIntent(context: Context, action: String, dialogId: String? = null): PendingIntent {
            val intent = Intent(context, PushBroadcastReceiver::class.java)
            intent.action = action
            intent.setPackage("live.eyezon.testsdkproject")
            dialogId?.apply {
                intent.putExtra(DIALOG_ID, this)
            }
            return PendingIntent.getBroadcast(
                context, (System.currentTimeMillis() % 1000).toInt(),
                intent, PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val dialogId = intent.extras?.getString(DIALOG_ID).orEmpty()
        when (intent.action) {
            ACTION_OPEN_APP -> openApp(context)
            ACTION_OPEN_DIALOG -> openDialog(context, dialogId)
        }
    }

    private fun openDialog(context: Context, dialogId: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(MainActivity.EXTRAS_DIALOG_ID, dialogId)
        }
        context.startActivity(intent)
    }

    private fun openApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_OPEN_APP
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}