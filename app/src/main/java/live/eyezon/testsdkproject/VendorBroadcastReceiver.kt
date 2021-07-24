package live.eyezon.testsdkproject

import android.content.Context
import com.witheyezon.sdk.domain.model.api.EyezonMessage
import com.witheyezon.sdk.tools.broadcast.EyezonBroadcastReceiver

class VendorBroadcastReceiver : EyezonBroadcastReceiver() {

    companion object {

        var onNewMessage: ((EyezonMessage) -> Unit)? = null
        var onConsoleEvent: ((String, String) -> Unit)? = null

        fun setOnNewMessageListener(listener: ((EyezonMessage) -> Unit)?) {
            onNewMessage = listener
        }

        fun setOnConsoleEventListener(listener: ((String, String) -> Unit)?) {
            onConsoleEvent = listener
        }
    }

    override fun onNewMessage(context: Context, message: EyezonMessage) {
        println("RE:: onNewMessage: messageId=${message._id}")
        onNewMessage?.invoke(message)
    }

    override fun onDialogDeleted(context: Context, dialogId: String) {
        println("RE:: onDialogDeleted: dialogId=$dialogId")
    }

    override fun onDialogReturned(context: Context, dialogId: String) {
        println("RE:: onDialogReturned: dialogId=$dialogId")
    }

    override fun onConsoleEvent(context: Context, eventName: String, event: String) {
        println("RE:: onConsoleEvent $eventName -> $event")
        onConsoleEvent?.invoke(eventName, event)
    }

    override fun onPushReceived(context: Context, title: String?, body: String?) {
        println("RE:: pushTitle -> $title")
        println("RE:: pushBody -> $body")
        EyezonNotificationManager(context).sendNotification(title.orEmpty(), body.orEmpty())
    }
}