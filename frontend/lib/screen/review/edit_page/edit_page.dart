import 'package:flutter/material.dart';
import 'package:http/http.dart' as http; // Import the http package
import 'dart:convert'; // for jsonEncode

class ReviewEditPage extends StatefulWidget {
  final Map<String, dynamic> review;

  const ReviewEditPage({super.key, required this.review});

  @override
  _ReviewEditPageState createState() => _ReviewEditPageState();
}

class _ReviewEditPageState extends State<ReviewEditPage> {
  late TextEditingController _titleController;
  late TextEditingController _contentController;
  late TextEditingController _rateController;

  Future<void> _updateReview() async {
    const url = "http://43.202.82.108:8080/api/v1/restaurants/reviews";

    final headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw',
    };

    final body = {
      "reviewId": widget.review["id"],
      "restaurantId": widget.review["restaurantId"],
      "title": _titleController.text,
      "content": _contentController.text,
      "rate": int.parse(_rateController.text),
    };

    final response = await http.patch(
      Uri.parse(url),
      headers: headers,
      body: jsonEncode(body),
    );

    if (response.statusCode == 200) {
      print("Review updated successfully");
    } else {
      print("Failed to update review. Status code: ${response.statusCode}");
    }
  }

  @override
  void initState() {
    super.initState();
    _titleController = TextEditingController(text: widget.review['title']);
    _contentController = TextEditingController(text: widget.review['content']);
    _rateController =
        TextEditingController(text: widget.review['rate'].toString());
  }

  @override
  void dispose() {
    _titleController.dispose();
    _contentController.dispose();
    _rateController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Edit Review"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _titleController,
              decoration: const InputDecoration(
                labelText: "Title",
              ),
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _contentController,
              decoration: const InputDecoration(
                labelText: "Content",
              ),
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _rateController,
              decoration: const InputDecoration(
                labelText: "Rate",
              ),
              keyboardType: TextInputType.number,
            ),
            ElevatedButton(
              onPressed: () async {
                await _updateReview();
                Navigator.pop(context); // Navigate back after saving.
              },
              child: const Text("Save Changes"),
            )
          ],
        ),
      ),
    );
  }
}
