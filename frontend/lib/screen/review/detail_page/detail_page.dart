import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:new_nawabari/screen/review/edit_page/edit_page.dart';

class ReviewDetailPage extends StatelessWidget {
  final Map<String, dynamic> review;
  // Assume this represents the currently logged-in user's memberId.
  final int currentMemberId = 22; // Example value

  const ReviewDetailPage({super.key, required this.review});

  Future<void> _deleteReview(BuildContext context) async {
    const String url = 'http://43.202.82.108:8080/api/v1/restaurants/reviews';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw', // Your token here
    };
    final body = jsonEncode(
        {"reviewId": review['id'], "restaurantId": review['restaurantId']});

    final response =
        await http.delete(Uri.parse(url), headers: headers, body: body);

    if (response.statusCode == 200) {
      Navigator.pop(
          context); // Close the review detail page after successful deletion.
    } else {
      print('Failed to delete review: ${response.body}');
      // You might want to show an error message to the user.
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Review Details"),
        // actions: review['memberId'] == currentMemberId
        actions: 22 == currentMemberId
            ? [
                IconButton(
                  icon: const Icon(Icons.edit),
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => ReviewEditPage(review: review),
                      ),
                    );
                  },
                ),
                IconButton(
                  icon: const Icon(Icons.delete),
                  onPressed: () {
                    // Show a confirmation dialog before deleting
                    showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return AlertDialog(
                          title: const Text('Delete Review'),
                          content: const Text(
                              'Are you sure you want to delete this review?'),
                          actions: [
                            TextButton(
                              child: const Text('Cancel'),
                              onPressed: () {
                                Navigator.of(context).pop(); // Close the dialog
                              },
                            ),
                            TextButton(
                              child: const Text('Delete'),
                              onPressed: () {
                                _deleteReview(context);
                              },
                            ),
                          ],
                        );
                      },
                    );
                  },
                )
              ]
            : [], // Empty actions list if memberId doesn't match.
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(
              review['title'],
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 16.0),
            Text(review['content']),
            const SizedBox(height: 16.0),
            Text("Rating: ${review['rate'].toString()}"),
            // Add more fields and styles as necessary.
          ],
        ),
      ),
    );
  }
}
