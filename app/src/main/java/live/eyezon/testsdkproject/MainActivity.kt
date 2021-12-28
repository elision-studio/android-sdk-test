package live.eyezon.testsdkproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.witheyezon.sdk.domain.model.other.SDKData
import com.witheyezon.sdk.domain.model.other.SDKUi
import com.witheyezon.sdk.tools.broadcast.IBroadcastListener
import com.witheyezon.sdk.tools.init.EyezonBusinessSDK
import com.witheyezon.sdk.tools.init.ServerArea
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL =
            "https://storage.googleapis.com/eyezonfortest/test-widget/webview.html?"
        private const val EYEZON_WIDGET_URL = "${BASE_URL}eyezon"

        private const val EYEZON_BUSINESS_ID = "5d63fe246c2590002eecef83"
        private const val EYEZON_BUTTON_ID = "5ec26f248107de3797f0807c"

        const val EXTRAS_DIALOG_ID = "MainActivity.extras.dialog.id"
    }

    private val predefinedData = SDKData(
        businessId = EYEZON_BUSINESS_ID,
        buttonId = EYEZON_BUTTON_ID,
        widgetUrl = EYEZON_WIDGET_URL
    )

    private val ui = SDKUi(
        statusBarColor = R.color.purple_700,
        toolbarColor = R.color.purple_700,
        toolbarTextColor = R.color.white
    )

    private var unreadMessages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EyezonBusinessSDK.initSdk(application, ServerArea.SANDBOX)
        EyezonBusinessSDK.addListener(object : IBroadcastListener {
            override fun onConsoleEvent(eventName: String, event: String) {
                println("RE:: onConsoleEvent $eventName -> $event")
            }

            override fun onPushReceived(title: String?, body: String?) {
                println("RE:: pushTitle -> $title")
                println("RE:: pushBody -> $body")
                EyezonNotificationManager(this@MainActivity).sendNotification(
                    title.orEmpty(), body.orEmpty()
                )
            }
        })
        btnClearFcmToken.setOnClickListener {
            EyezonBusinessSDK.removeToken({
                println("RE:: success")
            }, { error ->
                println("RE:: ${error.errorMessage}")
            })
        }
        btnOpenSdkParams.setOnClickListener {
            predefinedData.run {
                this@MainActivity.businessId.setText(businessId)
                this@MainActivity.buttonId.setText(buttonId)
                this@MainActivity.widgetUrl.setText(widgetUrl)
                this@MainActivity.headerText.setText("Sample text")
            }
        }
        btnOpenSdkInputParams.setOnClickListener {
            val widgetUrl = widgetUrl.text.toString()

            val widgetToLoad = if (widgetUrl.isEmpty()) predefinedData.widgetUrl else widgetUrl
            val data = SDKData(
                businessId = businessId.text.toString(),
                buttonId = buttonId.text.toString(),
                widgetUrl = widgetToLoad
            )
            val headerText = headerText.text.toString()
            val ui = if (headerText.isNotEmpty()) {
                ui.copy(toolbarText = headerText)
            } else {
                ui
            }
            EyezonBusinessSDK.openButton(data, ui)
            unreadMessages = 0
        }
        intent?.getStringExtra(EXTRAS_DIALOG_ID)?.run {
            val url = BASE_URL
                .plus("&open=true&dialogId=$this&notificationType=push")
            val data = predefinedData.copy(widgetUrl = url)
            EyezonBusinessSDK.openButton(data, ui.copy(toolbarText = "From Push"))
            unreadMessages = 0
        }
        btnOpenSdk.setOnClickListener {
            EyezonBusinessSDK.openButton(predefinedData, ui.copy(toolbarText = "From New Message"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EyezonBusinessSDK.removeAllListeners()
    }
}