package live.eyezon.testsdkproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.witheyezon.sdk.domain.model.other.SDKData
import com.witheyezon.sdk.domain.model.other.SDKUi
import com.witheyezon.sdk.tools.init.EyezonBusinessSDK
import com.witheyezon.sdk.tools.init.ServerArea
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EYEZON_WIDGET_URL =
            "https://storage.googleapis.com/eyezonfortest/test-widget/webview.html?eyezon&putInCart=true&eyezonRegion=sandbox&language=ru&target=SKU-1&title=Samsung%20Television&name=Test&phone=%2B3801111111111&email=test@test.test&clientId=test123&region=Brest"

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
        EyezonBusinessSDK.initReceiver(VendorBroadcastReceiver::class.java.name)
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
            val url = EYEZON_WIDGET_URL
                .plus("&notificationType=push")
                .plus("&dialogId=")
                .plus(this)
            val data = predefinedData.copy(widgetUrl = url)
            EyezonBusinessSDK.openButton(data, ui.copy(toolbarText = "From Push"))
            unreadMessages = 0
        }
        btnOpenSdk.setOnClickListener {
            EyezonBusinessSDK.openButton(predefinedData, ui.copy(toolbarText = "From New Message"))
        }
        VendorBroadcastReceiver.setOnNewMessageListener {
            unreadMessages += 1
            tvNewMessages.text = "New Messages: $unreadMessages"
        }
        VendorBroadcastReceiver.setOnConsoleEventListener { eventName, event ->
            if (eventName == "PUT_IN_CART_FROM_WIDGET") {
                Toast.makeText(this, event, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VendorBroadcastReceiver.setOnNewMessageListener(null)
        VendorBroadcastReceiver.setOnConsoleEventListener(null)
    }
}