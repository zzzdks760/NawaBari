import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:new_nawabari/screen/about_me/edit_name_page.dart';
import 'package:new_nawabari/screen/about_me/edit_phone_number_page.dart';
import 'package:new_nawabari/screen/about_me/edit_profile_image_page.dart';

class UserInfo {
  final String profileNickname;
  final String profileImage;

  UserInfo({
    required this.profileNickname,
    required this.profileImage,
  });

  factory UserInfo.fromJson(Map<String, dynamic> json) {
    return UserInfo(
      profileNickname: json['profile_nickname'],
      profileImage: json['profile_image'],
    );
  }
}

class MyInfoEditPage extends StatefulWidget {
  const MyInfoEditPage({super.key});

  @override
  _MyInfoEditPageState createState() => _MyInfoEditPageState();
}

class _MyInfoEditPageState extends State<MyInfoEditPage> {
  Future<Map<String, dynamic>> _fetchMyInfo() async {
    const String url = 'http://43.202.82.108:8080/api/v1/MyPage';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw',
    };

    const memberId = 22;

    final response = await http.get(
      Uri.parse('$url?id=$memberId'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      return json.decode(utf8.decode(response.bodyBytes));
    } else {
      throw Exception('Failed to load user info');
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Map<String, dynamic>>(
      future: _fetchMyInfo(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Scaffold(
              body: Center(child: CircularProgressIndicator()));
        } else if (snapshot.hasError) {
          return Scaffold(
              body: Center(child: Text('Error: ${snapshot.error}')));
        } else if (!snapshot.hasData) {
          return const Scaffold(body: Center(child: Text('No data available')));
        } else {
          UserInfo userInfo = UserInfo.fromJson(snapshot.data!);

          return Scaffold(
            appBar: AppBar(
              title: const Text('Edit my information'),
            ),
            body: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  Center(
                    child: GestureDetector(
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (BuildContext context) =>
                                EditProfileImagePage(),
                          ),
                        );
                      },
                      child: CircleAvatar(
                        radius: 50,
                        backgroundImage: NetworkImage(userInfo.profileImage),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  ListTile(
                    title: const Text('Name'),
                    trailing: Text(userInfo.profileNickname),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (BuildContext context) =>
                              const EditNamePage(),
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
          );
        }
      },
    );
  }
}
