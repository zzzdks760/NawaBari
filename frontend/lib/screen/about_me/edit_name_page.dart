import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:new_nawabari/screen/about_me/my_info_edit_page.dart';

class EditNamePage extends StatefulWidget {
  const EditNamePage({super.key});

  @override
  _EditNamePageState createState() => _EditNamePageState();
}

class _EditNamePageState extends State<EditNamePage> {
  late TextEditingController _nameController;
  final FocusNode _focusNode = FocusNode();

  String username = '';
  String profileImage = '';
  List<String> dongNames = [];

  Future<void>? _fetchInfoFuture;

  @override
  void initState() {
    super.initState();
    _fetchInfoFuture = _fetchMyInfo(); // Assign the future here.
  }

  Future<void> _fetchMyInfo() async {
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
      final jsonResponse = json.decode(utf8.decode(response.bodyBytes));

      setState(() {
        username = jsonResponse['profile_nickname'];
        profileImage = jsonResponse['profile_image'];
        dongNames = List<String>.from(jsonResponse['dongNames']);
      });
    } else {
      throw Exception('Failed to load user info');
    }
  }

  Future<void> _patchNewName(String newName) async {
    const String url = 'http://43.202.82.108:8080/api/v1/MyPage';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw', // truncated for brevity
    };

    final body = jsonEncode({
      "memberId": 22,
      "profile_nickname": newName,
      "profile_image":
          "http://k.kakaocdn.net/dn/bfj5fH/btrQZ2NJzCv/k1EwKjC7K00MPaTneQW5E1/img_110x110.jpg"
    });

    final response =
        await http.patch(Uri.parse(url), headers: headers, body: body);

    if (response.statusCode == 200) {
      final responseData = jsonDecode(response.body);
      Navigator.of(context).popUntil((route) => route.isFirst);

      // Push the fresh instance of MyInfoEditPage
      Navigator.of(context).push(
        MaterialPageRoute(
          builder: (context) => const MyInfoEditPage(),
        ),
      );
    } else {
      // Handle error
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to update name.')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: _fetchInfoFuture, // Use the _fetchInfoFuture here.
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Scaffold(
              body: Center(child: CircularProgressIndicator()));
        } else if (snapshot.hasError) {
          return Scaffold(
              body: Center(child: Text('Error: ${snapshot.error}')));
        } else {
          _nameController = TextEditingController(text: username);

          return Scaffold(
            appBar: AppBar(
              title: const Text('Edit Name'),
            ),
            body: Column(
              children: [
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: TextField(
                      controller: _nameController,
                      focusNode: _focusNode,
                      decoration: InputDecoration(
                        hintText: username,
                      ),
                    ),
                  ),
                ),
                SizedBox(
                  width: double.infinity,
                  child: Padding(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 16.0, vertical: 8.0),
                    child: ElevatedButton(
                      onPressed: () async {
                        await _patchNewName(_nameController.text);
                      },
                      child: const Text('Done'),
                    ),
                  ),
                ),
              ],
            ),
          );
        }
      },
    );
  }

  @override
  void dispose() {
    _nameController.dispose();
    _focusNode.dispose();
    super.dispose();
  }
}
