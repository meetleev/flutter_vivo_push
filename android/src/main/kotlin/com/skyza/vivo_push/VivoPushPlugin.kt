package com.skyza.vivo_push

import LogLevel
import PushInterface
import QueryProfileIdsResp
import ResultResp
import TokenResp
import android.app.Application
import com.vivo.push.PushClient
import com.vivo.push.PushConfig
import com.vivo.push.listener.IPushQueryActionListener
import com.vivo.push.restructure.request.IPushRequestCallback
import com.vivo.push.ups.TokenResult
import com.vivo.push.ups.UPSRegisterCallback
import com.vivo.push.ups.VUpsManager
import com.vivo.push.util.VivoPushException
import io.flutter.embedding.engine.plugins.FlutterPlugin


/** VivoPushPlugin */
class VivoPushPlugin : FlutterPlugin, PushInterface {

    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null
    private var mApplication: Application? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        pluginBinding = flutterPluginBinding
        PushInterface.setUp(flutterPluginBinding.binaryMessenger, this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    }

    override fun setLogLevel(
        level: LogLevel,
        callback: (Result<Unit>) -> Unit
    ) {
        LogUtils.setLogLevel(level)
    }

    override fun getLogLevel(callback: (Result<LogLevel>) -> Unit) {
        callback(Result.success(LogUtils.getLogLevel()))
    }

    override fun initialize(callback: (Result<Unit>) -> Unit) {
        mApplication = pluginBinding?.applicationContext as Application?
        //初始化push
        try {
            val config = PushConfig.Builder()
                .agreePrivacyStatement(true)
                .build()
            PushClient.getInstance(mApplication).initialize(config)
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun openPush(callback: (Result<Long>) -> Unit) {
        try {
            PushClient.getInstance(mApplication)
                .turnOnPush { state -> // 开关状态处理， 0代表成功，获取regid建议在state=0后获取；
                    callback(Result.success(state.toLong()))
                }
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun closePush(callback: (Result<Long>) -> Unit) {
        try {
            PushClient.getInstance(mApplication)
                .turnOffPush { state -> // 开关状态处理， 0代表成功，获取regid建议在state=0后获取；
                    callback(Result.success(state.toLong()))
                }
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun getRegId(callback: (Result<TokenResp>) -> Unit) {
        try {
            PushClient.getInstance(mApplication).getRegId(object : IPushQueryActionListener {
                override fun onSuccess(regid: String?) {
                    //获取成功，回调参数即是当前应用的regid；
                    callback(Result.success(TokenResp(regid)))
                }

                override fun onFail(errerCode: Int?) {
                    //获取失败，可以结合错误码参考查询失败原因；
                    callback(Result.success(TokenResp(null, errerCode?.toLong())))
                }
            })
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun deleteRegId(callback: (Result<Long>) -> Unit) {
        try {
            PushClient.getInstance(mApplication).deleteRegid { state ->
                callback(
                    Result.success(state.toLong())

                )
            }
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun registerToken(
        appId: String,
        appKey: String,
        appSecret: String,
        callback: (Result<TokenResp>) -> Unit
    ) {
        try {
            VUpsManager.getInstance()
                .registerToken(
                    mApplication,
                    appId,
                    appKey,
                    appSecret,
                    object : UPSRegisterCallback {
                        override fun onResult(tokenResult: TokenResult) {
                            if (tokenResult.returnCode == 0) {
                                LogUtils.debug("注册成功 regID=" + tokenResult.token)
                                callback(Result.success(TokenResp(tokenResult.token)))
                            } else {
                                LogUtils.warn("注册失败")
                                callback(
                                    Result.success(
                                        TokenResp(
                                            null,
                                            tokenResult.returnCode.toLong()
                                        )
                                    )
                                )
                            }
                        }
                    })
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun unregisterToken(callback: (Result<ResultResp>) -> Unit) {

        try {
            VUpsManager.getInstance()
                .unRegisterToken(
                    mApplication,
                    object : UPSRegisterCallback {
                        override fun onResult(tokenResult: TokenResult) {
                            if (tokenResult.returnCode == 0) {
                                LogUtils.debug("解注册成功 regID=" + tokenResult.token)
                                callback(Result.success(ResultResp(true)))
                            } else {
                                LogUtils.warn("解注册失败")
                                callback(
                                    Result.success(
                                        ResultResp(
                                            false,
                                            tokenResult.returnCode.toLong()
                                        )
                                    )
                                )
                            }
                        }
                    })
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun setTopic(topic: String, callback: (Result<Long>) -> Unit) {
        try {
            PushClient.getInstance(mApplication).setTopic(
                topic
            ) { state -> callback(Result.success(state.toLong())) }

        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun deleteTopic(topic: String, callback: (Result<Long>) -> Unit) {
        try {
            PushClient.getInstance(mApplication).deleteRegid { state ->
                callback(Result.success(state.toLong()))
            }
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun getTopics(callback: (Result<List<String>>) -> Unit) {
        try {
            val topics = PushClient.getInstance(mApplication).topics
            callback(Result.success(topics))
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun isSupport(callback: (Result<Boolean>) -> Unit) {
        try {
            val isSupport = PushClient.getInstance(mApplication).isSupport
            callback(Result.success(isSupport))
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun isPushSupport(flag: Long, callback: (Result<Boolean>) -> Unit) {
        try {
            val isSupport = PushClient.getInstance(mApplication).isPushSupport(flag.toInt())
            callback(Result.success(isSupport))
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun isSupportSyncProfileInfo(callback: (Result<Boolean>) -> Unit) {
        try {
            val isSupport = 0 == PushClient.getInstance(mApplication).isSupportSyncProfileInfo
            callback(Result.success(isSupport))
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun addProfileId(profileId: String, callback: (Result<ResultResp>) -> Unit) {
        try {
            PushClient.getInstance(mApplication)
                .addProfileId(profileId, object : IPushRequestCallback<Int?> {
                    override fun onSuccess(obj: Int?) {
                        LogUtils.debug("设置profileId成功，profileId = $profileId")
                        callback(Result.success(ResultResp(true)))
                    }

                    override fun onError(code: Int) {
                        // 参数code说明失败原因，参考：公共状态码
                        LogUtils.warn(
                            "设置profileId失败，profileId = $profileId, errCode = $code"
                        )
                        callback(Result.success(ResultResp(false, code.toLong())))

                    }
                })
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun deleteProfileId(profileId: String, callback: (Result<ResultResp>) -> Unit) {
        try {
            PushClient.getInstance(mApplication)
                .deleteProfileId(profileId, object : IPushRequestCallback<Int?> {
                    override fun onSuccess(obj: Int?) {
                        LogUtils.debug("删除profileId成功，profileId = $profileId")
                        callback(Result.success(ResultResp(true)))
                    }

                    override fun onError(code: Int) {
                        // 参数code说明失败原因，参考：公共状态码
                        LogUtils.warn("删除profileId失败，profileId = $profileId, errCode = $code")
                        callback(Result.success(ResultResp(false, code.toLong())))

                    }
                })
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun deleteAllProfileId(callback: (Result<ResultResp>) -> Unit) {
        try {
            PushClient.getInstance(mApplication)
                .deleteAllProfileId(object : IPushRequestCallback<Int?> {
                    override fun onSuccess(obj: Int?) {
                        LogUtils.debug("删除全部profileId成功")
                        callback(Result.success(ResultResp(true)))
                    }

                    override fun onError(code: Int) {
                        // 参数code说明失败原因，参考：公共状态码
                        LogUtils.warn("删除全部profileId失败, errCode = $code")
                        callback(Result.success(ResultResp(false, code.toLong())))
                    }
                })
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun queryProfileIds(callback: (Result<QueryProfileIdsResp>) -> Unit) {
        try {
            PushClient.getInstance(mApplication)
                .queryProfileIds(object : IPushRequestCallback<MutableList<String?>?> {
                    override fun onSuccess(data: MutableList<String?>?) {
                        if (data != null && data.isNotEmpty()) {

                            LogUtils.debug("应用所有的profileIds : $data")
                        } else {
                            LogUtils.debug("应用无profileId")
                        }
                        callback(
                            Result.success(
                                QueryProfileIdsResp(
                                    data?.filterNotNull()?.toList() ?: emptyList()
                                )
                            )
                        )
                    }

                    override fun onError(code: Int) {
                        //参数code说明失败原因，参考：公共状态码
                        LogUtils.warn("查询profileId失败, errCode = $code")
                        callback(Result.success(QueryProfileIdsResp(emptyList(), code.toLong())))
                    }
                })
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }

    override fun querySubscribeState(callback: (Result<Long>) -> Unit) {
        try {
            PushClient.getInstance(mApplication)
                .querySubscribeState { substate -> // substate = 0:已订阅状态；1：未订阅；其它：异常情况,参考：公共状态码
                    LogUtils.debug(" substate= $substate")
                    callback(Result.success(substate.toLong()))
                }
        } catch (e: VivoPushException) {
            e.printStackTrace()
            callback(Result.failure(e))
        }
    }
}
