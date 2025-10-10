import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/messages.g.dart',
  dartOptions: DartOptions(),
  kotlinOut: 'android/src/main/kotlin/com/skyza/vivo_push/Messages.g.kt',
  kotlinOptions: KotlinOptions(),
  // copyrightHeader: 'pigeons/copyright.txt',
  dartPackageName: 'vivo_push',
))
class TokenResp {
  final String? token;

  /// see https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  final int? errCode;

  TokenResp({this.token, this.errCode});
}

class ResultResp {
  final bool success;

  /// see https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9

  final int? errCode;

  ResultResp({required this.success, this.errCode});
}

class QueryProfileIdsResp {
  final List<String> profileIds;

  /// see https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  final int? errCode;

  QueryProfileIdsResp({required this.profileIds, required this.errCode});
}

enum LogLevel { verbose, debug, info, warn, error, off }

@HostApi()
abstract class PushInterface {
  @async
  void setLogLevel(LogLevel level);
  @async
  LogLevel getLogLevel();

  /// 初始化push服务
  @async
  void initialize();

  /// 打开应用push开关，订阅应用，成功后便可接收到当前应用的推送消息,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  @async
  int openPush();

  /// 关闭应用push开关，解订阅应用，解订阅后将收不到当前应用的推送消息,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  @async
  int closePush();

  /// 获取当前设备的当前应用的唯一标识，后台可基于此标识发送通知
  @async
  TokenResp getRegId();

  /// 删除当前regid,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  @async
  int deleteRegId();

  /// 注册push，获取申请的regId
  @async
  TokenResp registerToken(
      {required String appId,
      required String appKey,
      required String appSecret});

  /// 解注册push，关闭push功能
  @async
  ResultResp unregisterToken();

  /// 设置标签,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  @async
  int setTopic(String topic);

  /// 删除标签,  返回0表示成功，参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  @async
  int deleteTopic(String topic);

  /// 获取当前设备的应用标签，后台可基于此标签发送通知
  @async
  List<String> getTopics();

  /// 用于判断当前系统是否支持push服务
  @async
  bool isSupport();

  /// 查询设备是否支持对应push功能
  @async
  bool isPushSupport(int flag);

  /// 是否支持profileId校验功能
  @async
  bool isSupportSyncProfileInfo();

  /// 添加应用的profileId, 添加ID只允许一次添加一个，单个profileId长度限制为64，单应用最多设置10个
  @async
  ResultResp addProfileId(String profileId);

  /// 删除应用指定的profileId
  @async
  ResultResp deleteProfileId(String profileId);

  /// 除该应用下所有的profileId
  @async
  ResultResp deleteAllProfileId();

  /// 查询应用所有的profileId
  @async
  QueryProfileIdsResp queryProfileIds();

  /// 查询应用的订阅状态
  /// 0:已订阅状态；1：未订阅；其它：异常情况,参考公共状态码 https://dev.vivo.com.cn/documentCenter/doc/364#s-2gw875g9
  @async
  int querySubscribeState();
}
