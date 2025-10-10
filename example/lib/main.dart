import 'package:flutter/material.dart';

import 'package:vivo_push/vivo_push.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _vivoPushPlugin = VivoPush();
  final appId = 0;
  final appKey = "";
  final appSecret = "";

  String _msg = '';
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('vivo_push_example'),
          ),
          body: Center(
            child: Column(
              children: [
                Text(_msg),
                TextButton(
                    onPressed: () {
                      _vivoPushPlugin.initialize();
                    },
                    child: Text('initialize')),
                TextButton(
                    onPressed: () async {
                      final errCode = await _vivoPushPlugin.openPush();
                      if (mounted) {
                        setState(() {
                          _msg = 'errCode:$errCode';
                        });
                      }
                    },
                    child: Text('openPush')),
                TextButton(
                    onPressed: () async {
                      final resp = await _vivoPushPlugin.registerToken(
                          appId: '$appId',
                          appKey: appKey,
                          appSecret: appSecret);
                      if (mounted) {
                        if (null != resp.token) {
                          setState(() {
                            _msg = 'token:${resp.token!}';
                          });
                        } else if (null != resp.errCode) {
                          setState(() {
                            _msg = 'errCode:${resp.errCode!}';
                          });
                        }
                      }
                    },
                    child: Text('registerToken')),
              ],
            ),
          )),
    );
  }
}
