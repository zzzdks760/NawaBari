import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class PreviewPage extends StatelessWidget {
  final String review;
  final List<Uint8List> selectedImages;
  final Map<String, dynamic> restaurant;
  final double rating;

  const PreviewPage({
    super.key,
    required this.review,
    required this.selectedImages,
    required this.restaurant,
    required this.rating,
  });

  Future<void> _postReview() async {
    const String url = "http://43.202.82.108:8080/api/v1/restaurants/reviews";
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw',
    };
    List<String> base64Images =
        selectedImages.map((imageData) => base64Encode(imageData)).toList();

    var request = http.MultipartRequest('POST', Uri.parse(url));
    request.headers.addAll(headers);

    request.fields['memberId'] = '22';
    request.fields['restaurantId'] = restaurant['restaurantId'].toString();
    request.fields['title'] = review;
    request.fields['content'] = '';
    request.fields['rate'] = rating.toInt().toString();

    for (int i = 0; i < base64Images.length; i++) {
      request.fields['photos[$i]'] = base64Images[i];
    }

    var response = await request.send();
    print(response.statusCode);
    print(await response.stream.bytesToString());

    if (response.statusCode == 200) {
      // Handle success case
    } else {
      // Handle error case
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Preview'),
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Padding(
              padding: EdgeInsets.all(8.0),
              child: Row(
                children: [
                  CircleAvatar(
                    backgroundImage: NetworkImage(
                        'https://via.placeholder.com/50'), // User's profile picture
                    radius: 25,
                  ),
                  SizedBox(width: 8.0),
                  Text(
                    'Username', // Replace with the actual username
                    style: TextStyle(fontSize: 16),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Center(
                child: Text(
                  review,
                  textAlign: TextAlign.center,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                for (final imageData in selectedImages)
                  SizedBox(
                    width: MediaQuery.of(context).size.width /
                        3.3, // Adjust the width here
                    height: MediaQuery.of(context).size.width / 3.3,
                    child: Image.memory(
                      imageData,
                      fit: BoxFit.cover,
                    ),
                  ),
              ],
            ),
            ListTile(
              leading: IconButton(
                onPressed: () {
                  // Implement like functionality
                },
                icon: const Icon(Icons.favorite_border),
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: Padding(
        padding: const EdgeInsets.all(8.0),
        child: ElevatedButton(
          onPressed: _postReview,
          child: const Text('Post Review'),
        ),
      ),
    );
  }
}
