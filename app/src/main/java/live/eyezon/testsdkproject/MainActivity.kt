package live.eyezon.testsdkproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.witheyezon.sdk.domain.model.other.SDKData
import com.witheyezon.sdk.tools.init.EyezonBusinessSDK
import com.witheyezon.sdk.tools.init.ServerArea
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val predefinedData = SDKData(
        "5d63fe246c2590002eecef83",
        "5ec26f248107de3797f0807c",
        "Mikhail Yarashevich",
        "bigbottleapps@gmail.com",
        "2982114490",
        "Test from sdk ${System.currentTimeMillis()}"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EyezonBusinessSDK.initSdk(application, ServerArea.SANDBOX)
        EyezonBusinessSDK.initReceiver(VendorBroadcastReceiver::class.java.name)
        btnOpenSdkParams.setOnClickListener {
            EyezonBusinessSDK.openButton(predefinedData)
        }
        btnOpenSdk.setOnClickListener {
            val data = SDKData(
                autoSubmit = etAutoSubmit.text.toString(),
                businessId = etBusinessId.text.toString(),
                buttonId = etButtonId.text.toString(),
                language = etLanguage.text.toString(),
                region = etRegion.text.toString(),
                target = etTarget.text.toString(),
                title = etTitle.text.toString(),
                userEmail = etUserEmail.text.toString(),
                userName = etUserName.text.toString(),
                userPhone = etUserPhone.text.toString()
            )
            EyezonBusinessSDK.openButton(data)
        }
    }
}