package live.eyezon.testsdkproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.witheyezon.sdk.domain.model.other.SDKData
import com.witheyezon.sdk.domain.model.other.SDKUi
import com.witheyezon.sdk.tools.init.EyezonBusinessSDK
import com.witheyezon.sdk.tools.init.ServerArea
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EYEZON_WIDGET_URL =
            "https://storage.googleapis.com/eyezonfortest/test-widget/webview.html?eyezon"

        private const val EYEZON_BUSINESS_ID = "5d63fe246c2590002eecef83"
        private const val EYEZON_BUTTON_ID = "5ec26f248107de3797f0807c"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EyezonBusinessSDK.initSdk(application, ServerArea.RUSSIA)
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
        }
    }
}