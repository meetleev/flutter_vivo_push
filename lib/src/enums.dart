enum SupportPushFlag {
  /// 查询是否支持“账号校验”功能的标记位
  profileIdFlag(1 << 2),

  /// 查询是否支持“消息覆盖更新”功能的标记位
  messageCoverageFlag(1 << 8),

  /// 查询是否支持“前台应用消息不展示”功能的标记位
  noShowOnForegroundFlag(1 << 9),

  /// 查询是否支持“定时展示”功能的标记位
  delayDispatchFlag(1 << 10);

  const SupportPushFlag(this.value);
  final int value;
}

