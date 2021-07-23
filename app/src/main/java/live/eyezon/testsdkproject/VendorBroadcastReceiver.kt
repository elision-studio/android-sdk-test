package live.eyezon.testsdkproject

import com.witheyezon.sdk.domain.model.api.EyezonMessage
import com.witheyezon.sdk.tools.broadcast.EyezonBroadcastReceiver

class VendorBroadcastReceiver : EyezonBroadcastReceiver() {

    override fun onNewMessage(message: EyezonMessage) {
        println("RE:: onNewMessage: messageId=${message._id}")
    }

    override fun onDialogDeleted(dialogId: String) {
        println("RE:: onDialogDeleted: dialogId=$dialogId")
    }

    override fun onDialogReturned(dialogId: String) {
        println("RE:: onDialogReturned: dialogId=$dialogId")
    }

    override fun onConsoleEvent(eventName: String, event: String) {
        println("RE:: onConsoleEvent $eventName -> $event")
    }

    override fun onPushReceived(title: String?, body: String?) {
        println("RE:: pushTitle -> $title")
        println("RE:: pushBody -> $body")
    }
}