import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:new_nawabari/screen/about_me/my_info_edit_page.dart';
import 'package:new_nawabari/screen/about_me/my_review_page.dart';
import 'package:new_nawabari/screen/settings/settings.dart';
import 'package:new_nawabari/screen/review/detail_page/detail_page.dart';

class MyInfoPage extends StatefulWidget {
  const MyInfoPage({super.key});

  @override
  _MyInfoPageState createState() => _MyInfoPageState();
}

class _MyInfoPageState extends State<MyInfoPage> {
  int reviewCount = 0;
  String username = 'Loading...';
  String profileImage = 'https://via.placeholder.com/150'; // Default image
  List<dynamic> myReviews = [];
  List<String> dongNames = [];
  int memberId = 22;

  @override
  void initState() {
    super.initState();
    _fetchMyInfo();
    _fetchMyReviews();
  }

  Future<void> _fetchMyInfo() async {
    const String url = 'http://43.202.82.108:8080/api/v1/MyPage';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MjY4MTE4OSwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.doLK-HkZX0Od1akSxrtESYNnW9KUG5UWVfC-P0XcvTcgtCeoNKP6znC10eMngdSy_RB87VCQScsOJfjrfFPm1g',
    };

    final response = await http.get(
      Uri.parse('$url?id=$memberId'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      final jsonResponse = json.decode(utf8.decode(response.bodyBytes));

      setState(() {
        username = jsonResponse['profile_nickname'];
        profileImage = jsonResponse['profile_image'];
        dongNames = List<String>.from(jsonResponse['dongNames']);
      });
    } else {
      // Handle error
      print('Failed to load user info');
    }
  }

  Future<void> _fetchMyReviews() async {
    String url =
        'http://43.202.82.108:8080/api/v1/MyPage/reviews?memberId=$memberId';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MjY4MTE4OSwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.doLK-HkZX0Od1akSxrtESYNnW9KUG5UWVfC-P0XcvTcgtCeoNKP6znC10eMngdSy_RB87VCQScsOJfjrfFPm1g', // 여기에 해당하는 토큰을 넣어주세요
    };

    final Map<String, dynamic> body = {"memberId": 22};

    final request = http.Request('GET', Uri.parse(url))
      ..headers.addAll(headers);

    final response = await request.send();

    if (response.statusCode == 200) {
      final responseBody = await response.stream.toBytes();
      final responseBodyString = utf8.decode(responseBody);
      final jsonResponse = json.decode(responseBodyString);
      setState(() {
        myReviews = jsonResponse['content'];
        reviewCount = myReviews.length; // 리뷰의 수를 업데이트
      });
    } else {
      // Handle error
      print('Failed to load reviews');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('My Info'),
        actions: [
          IconButton(
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (BuildContext context) => const MyInfoEditPage(),
                ),
              );
            },
            icon: const Icon(Icons.edit),
          ),
          IconButton(
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (BuildContext context) => const SettingsPage(),
                ),
              );
            },
            icon: const Icon(Icons.settings),
          ),
        ],
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Text(
              'My Information',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
          ),
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: CircleAvatar(
                  radius: 30,
                  backgroundImage: NetworkImage(profileImage),
                ),
              ),
              Text(
                username,
                style:
                    const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
            ],
          ),
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Text(
              '리뷰가 작성가능한 구역',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              dongNames.join(", "),
              style: const TextStyle(fontSize: 16),
            ),
          ),
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Text(
              'Reviews written by me',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
          ),
          GestureDetector(
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (BuildContext context) =>
                      MyReviewPage(myReviews: myReviews),
                ),
              );
            },
            child: Column(
              children: [
                const Text(
                  'Total Reviews',
                  style: TextStyle(fontSize: 16),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(
                    reviewCount.toString(),
                    style: const TextStyle(
                        fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                ),
              ],
            ),
          ),
          // Expanded(
          //   child: ListView.builder(
          //     itemCount: myReviews.length,
          //     itemBuilder: (BuildContext context, int index) {
          //       return InkWell(
          //         onTap: () {
          //           Navigator.push(
          //             context,
          //             MaterialPageRoute(
          //               builder: (context) =>
          //                   ReviewDetailPage(review: myReviews[index]),
          //             ),
          //           );
          //         },
          //         child: ListTile(
          //           title: Text(myReviews[index]['title']),
          //           subtitle: Text(myReviews[index]['content']),
          //           trailing: Text(myReviews[index]['rate'].toString()),
          //         ),
          //       );
          //     },
          //   ),
          // ),
        ],
      ),
    );
  }
}
