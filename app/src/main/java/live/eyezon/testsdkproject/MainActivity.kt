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
                etAutoSubmit.text.toString(),
                etBusinessId.text.toString(),
                etButtonId.text.toString(),
                etLanguage.text.toString(),
                etRegion.text.toString(),
                etTarget.text.toString(),
                etTitle.text.toString(),
                etUserEmail.text.toString(),
                etUserName.text.toString(),
                etUserPhone.text.toString()
            )
            EyezonBusinessSDK.openButton(data)
        }
    }
}