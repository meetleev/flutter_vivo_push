# vivo_push

[![Pub](https://img.shields.io/pub/v/vivo_push.svg?style=flat-square)](https://pub.dev/packages/vivo_push)
[![support](https://img.shields.io/badge/platform-android%20-blue.svg)](https://pub.dev/packages/vivo_push)

vivo 推送API (vpush)

一个轻量级 Flutter 插件，用于集成 vivo 推送 API。它简化了 vivo 手机平台的推送通知开发，支持 token 注册、消息接收和回调处理。

## Usage

``` dart
    import 'package:vivo_push/vivo_push.dart';

    final vivoPushPlugin = VivoPush();
    await vivoPushPlugin.initialize();
    final resp = await vivoPushPlugin.registerToken(
                            appId: 'appId',
                            appKey: 'appKey',
                            appSecret: 'appSecret');
    final regId = resp.token;

```
[example](./example/lib/main.dart)

## Additional information

具体参考:[vivo客户端API接口文档](https://dev.vivo.com.cn/documentCenter/doc/364)
