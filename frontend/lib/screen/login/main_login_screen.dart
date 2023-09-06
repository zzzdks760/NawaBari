import 'package:flutter/material.dart';
import 'package:new_nawabari/screen/main_page/home_page.dart'; // Import for MainPage
import 'package:http/http.dart' as http; // For making the HTTP request
import 'package:shared_preferences/shared_preferences.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk.dart';
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart';
import 'package:flutter/services.dart';
import 'package:uuid/uuid.dart';
import 'package:flutter_web_auth/flutter_web_auth.dart';
import 'dart:convert';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  _LoginPageState createState() => _LoginPageState();
}

Future<void> _saveTokensLocally(
    String accessToken, String refreshToken, String memberId) async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  await prefs.setString('access_token', accessToken);
  await prefs.setString('refresh_token', refreshToken);
  await prefs.setString('memberId', memberId);
}

class _LoginPageState extends State<LoginPage> {
  // Add this method to launch the URL
  Map<String, dynamic> result = {};

  // Future<Map<String, String>> signInWithKakao() async {
  Future<String> signInWithKakao() async {
    final clientState = const Uuid().v4();

    final url = Uri.https('kauth.kakao.com', '/oauth/authorize', {
      'response_type': 'code',
      'client_id': '09b48658d094b40532cc396778f00012',
      'redirect_uri': 'http://43.202.82.108:8080/login/oauth2/code/kakao',
      'state': clientState,
    });
    print('---------------------');
    print(url.toString());
    final result = await FlutterWebAuth.authenticate(
        url: url.toString(), callbackUrlScheme: "callback-scheme");
    // final result = Uri.https(url.toString());
    print('======================');
    print(result);
    // final body = Uri.parse(result).queryParameters;

    // print(body.toString());
    return result;
  }

  Future<Map<String, dynamic>> _launchAuthCode(String authCode) async {
    String url = 'http://43.202.82.108:8080/oauth2/callback/authorization';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
    };
    // 바디를 정의합니다.
    String body = '{"Authorization": "$authCode"}';

    // Request 객체를 사용하여 GET 요청을 생성합니다.
    var request = http.Request('GET', Uri.parse(url))
      ..headers.addAll(headers)
      ..body = body;

    var response = await request.send();
    if (response.statusCode == 200) {
      String body = await response.stream.bytesToString();

      // (옵션) JSON 응답인 경우, 해당 문자열을 디코드합니다.
      Map<String, dynamic> decodedData = jsonDecode(body);

      print(decodedData);
      return decodedData;
    } else {
      // Handle the error as needed.
      print('Request failed with status: ${response.statusCode}.');
      throw Exception('Failed to load data');
    }
  }

  Future<void> loginWithKakao() async {
    bool loginSuccess = false; // Flag to check login success

    if (await isKakaoTalkInstalled()) {
      try {
        // OAuthToken token = await UserApi.instance.loginWithKakaoTalk();
        // OAuthToken token = await UserApi.instance.loginWithKakaoAccount();
        // print('카카오톡으로 로그인 성공 $token');
        print(KakaoSdk.redirectUri);
        OAuthToken token = await UserApi.instance.loginWithKakaoTalk();
        print('카카오톡으로 로그인 성공 ${token.accessToken}');
        String authCode = await AuthCodeClient.instance.authorizeWithTalk(
          // clientId: "09b48658d094b40532cc396778f00012",
          // redirectUri: "http://43.202.82.108:8080/login/oauth2/code/kakao",
          redirectUri: KakaoSdk.redirectUri,
        );
        // redirectUri: KakaoSdk.redirectUri);
        print('카카오톡으로 로그인 성공 $authCode');
        result = await _launchAuthCode(authCode);
        print(result);
        loginSuccess = true;
      } catch (error) {
        print('카카오톡으로 로그인 실패 $error');
        if (error is PlatformException && error.code == 'CANCELED') {
          return;
        }
        try {
          await UserApi.instance.loginWithKakaoTalk();
          print('카카오계정으로 로그인 성공');
          loginSuccess = true;
        } catch (error) {
          print('카카오계정으로 로그인 실패 $error');
        }
      }
    } else {
      try {
        await UserApi.instance.loginWithKakaoAccount();
        print('카카오계정으로 로그인 성공');
        loginSuccess = true;
      } catch (error) {
        print('카카오계정으로 로그인 실패 $error');
      }
    }

    if (loginSuccess) {
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const MainPage()),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Login Page'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: signInWithKakao, // Change onPressed to _launchURL
              child: const Text('Log in with KakaoTalk'),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const MainPage()),
                );
              },
              child: const Text('Later'),
            ),
          ],
        ),
      ),
    );
  }
}
