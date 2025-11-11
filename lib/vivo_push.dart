import 'package:vivo_push/src/messages.g.dart';

import 'src/enums.dart';

export 'src/enums.dart';

class VivoPush {
  final PushInterface _pushInterface = PushInterface();

  factory VivoPush() => instance;

  static VivoPush get instance => VivoPush._();
  VivoPush._();

  Future<void> setLogLevel(LogLevel level) => _pushInterface.setLogLevel(level);
  Future<LogLevel> getLogLevel() => _pushInterface.getLogLevel();

  /// 初始化push服务
  Future<void> initialize() => _pushInterface.initialize();

  /// 打开应用push开关，订阅应用，成功后便可接收到当前应用的推送消息,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  Future<int> openPush() => _pushInterface.openPush();

  /// 关闭应用push开关，解订阅应用，解订阅后将收不到当前应用的推送消息,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  Future<int> closePush() => _pushInterface.closePush();

  /// 获取当前设备的当前应用的唯一标识，后台可基于此标识发送通知
  Future<TokenResp> getRegId() => _pushInterface.getRegId();

  /// 删除当前regid,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9

  Future<int> deleteRegId() => _pushInterface.deleteRegId();

  /// 注册push，获取申请的regId
  Future<TokenResp> registerToken({
    required String appId,
    required String appKey,
    required String appSecret,
  }) => _pushInterface.registerToken(
    appId: appId,
    appKey: appKey,
    appSecret: appSecret,
  );

  /// 解注册push，关闭push功能
  Future<ResultResp> unregisterToken() => _pushInterface.unregisterToken();

  /// 设置标签,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  Future<int> setTopic(String topic) => _pushInterface.setTopic(topic);

  /// 删除标签,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  Future<int> deleteTopic(String topic) => _pushInterface.deleteTopic(topic);

  /// 获取当前设备的应用标签，后台可基于此标签发送通知
  Future<List<String>> getTopics() => _pushInterface.getTopics();

  /// 用于判断当前系统是否支持push服务
  Future<bool> isSupport() => _pushInterface.isSupport();

  /// 查询设备是否支持对应push功能
  Future<bool> isPushSupport(SupportPushFlag flag) =>
      _pushInterface.isPushSupport(flag.value);

  /// 是否支持profileId校验功能
  Future<bool> isSupportSyncProfileInfo() =>
      _pushInterface.isSupportSyncProfileInfo();

  /// 添加应用的profileId, 添加ID只允许一次添加一个，单个profileId长度限制为64，单应用最多设置10个
  Future<ResultResp> addProfileId(String profileId) =>
      _pushInterface.addProfileId(profileId);

  /// 删除应用指定的profileId
  Future<ResultResp> deleteProfileId(String profileId) =>
      _pushInterface.deleteProfileId(profileId);

  /// 除该应用下所有的profileId
  Future<ResultResp> deleteAllProfileId() =>
      _pushInterface.deleteAllProfileId();

  /// 查询应用所有的profileId
  Future<QueryProfileIdsResp> queryProfileIds() =>
      _pushInterface.queryProfileIds();

  /// 查询应用的订阅状态
  /// 0:已订阅状态；1：未订阅；其它：异常情况,参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  Future<int> querySubscribeState() => _pushInterface.querySubscribeState();
}
