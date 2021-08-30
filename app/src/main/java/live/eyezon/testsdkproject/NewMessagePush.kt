package live.eyezon.testsdkproject

import com.google.gson.annotations.SerializedName

data class NewMessagePush(
    @SerializedName("type")
    var type: String,
    @SerializedName("dialogId")
    var dialogId: String,
    @SerializedName("userId")
    var userId: String
)