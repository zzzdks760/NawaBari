import 'package:flutter/material.dart';
import 'package:new_nawabari/screen/login/main_login_screen.dart';
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart';
import 'package:uni_links/uni_links.dart';

// 구역설정
// 식당 상세페이지내에서 리뷰작성
// 좋아요
// 리뷰수정  리뷰삭제
// 개인  프로필사진 변경
// 로그아웃, 탈퇴하기
// 로그인
// 스크롤시 페이징 처리

void main() {
  KakaoSdk.init(nativeAppKey: "18f952f0f1dbe4d4761234378f3bbf8d");
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  void handleDeepLink(Uri? deepLink) {
    print(deepLink);
    if (deepLink != null) {
      String? path = deepLink.path;
      print(path);
      // if (path == '/login') {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const LoginPage()),
      );
      // }
    }
  }

  @override
  void initState() {
    super.initState();

    // Initial link
    getInitialUri().then(handleDeepLink);

    // Listen for incoming links
    uriLinkStream.listen(handleDeepLink);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: const LoginPage(),
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
