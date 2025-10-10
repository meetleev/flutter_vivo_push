package com.skyza.vivo_push

import android.content.Context
import android.widget.Toast
import com.vivo.push.model.UPSNotificationMessage
import com.vivo.push.model.UnvarnishedMessage
import com.vivo.push.sdk.OpenClientPushMessageReceiver

class PushMessageReceiver : OpenClientPushMessageReceiver() {
    /*
    * regId结果返回。当开发者首次调用turnOnPush成功或regId发生改变时会回调该方法
      context : 应用上下文； regId: 当前设备的当前应用的唯一标识；
    */
    override fun onReceiveRegId(context: Context?, s: String?) {
        LogUtils.debug(" onReceiveRegId= $s")
    }

    override fun onTransmissionMessage(context: Context?, unvarnishedMessage: UnvarnishedMessage) {
        super.onTransmissionMessage(context, unvarnishedMessage)
        Toast.makeText(
            context,
            " 收到透传通知： " + unvarnishedMessage.message,
            Toast.LENGTH_LONG
        ).show()
        LogUtils.debug(" onTransmissionMessage= ${unvarnishedMessage.message}")
    }
    // 当应用在前台时可通过onForegroundMessageArrived接口，接收特定的前台不展示通知内容。
    override fun onForegroundMessageArrived(msg: UPSNotificationMessage?) {
        LogUtils.debug(
            " 收到前台不展示消息 onForegroundMessageArrived= $msg"
        )
    }


}