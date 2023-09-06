import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:new_nawabari/screen/review/detail_page/detail_page.dart';
import 'package:new_nawabari/widget/google_map.dart'; // Ensure this path is correct.

class DetailPage extends StatefulWidget {
  final Map<String, dynamic> restaurant;

  const DetailPage({super.key, required this.restaurant});

  @override
  _DetailPageState createState() => _DetailPageState();
}

class _DetailPageState extends State<DetailPage> {
  bool _isHoursExpanded = false;
  Map<String, dynamic> _restaurantDetails = {};
  int memberId = 22; // assuming you have memberId somewhere
  Set<int> likedReviews = {};

  Future<void> _likeReview(int reviewId, int memberId) async {
    const String url =
        'http://43.202.82.108:8080/api/v1/restaurant/review/heart';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw', // truncated for brevity
    };

    final body = jsonEncode({"reviewId": reviewId, "memberId": memberId});

    final response =
        await http.post(Uri.parse(url), headers: headers, body: body);

    if (response.statusCode == 200) {
      int result = int.parse(response.body);
      print(result);
      setState(() {
        if (result == 1) {
          likedReviews.add(reviewId);
        } else if (result == 0) {
          likedReviews.remove(reviewId);
        }
      });
    } else {
      // Handle error if necessary
      print('Failed to like review: ${response.body}');
    }
  }

  Future<void> fetchRestaurantDetails() async {
    const String url =
        'http://43.202.82.108:8080/api/v1/restaurants/restaurant';
    final int restaurantId = widget.restaurant['restaurantId'];
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw',
    };

    final response = await http.get(
      Uri.parse('$url?restaurantId=$restaurantId'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      final responseData = json.decode(utf8.decode(response.bodyBytes));
      setState(() {
        _restaurantDetails = responseData;
      });
    } else {
      print('Failed to load restaurant details');
      // Handle error accordingly
    }
  }

  List<Map<String, dynamic>> _reviews = [];

  Future<void> fetchRestaurantReviews() async {
    const String url = 'http://43.202.82.108:8080/api/v1/restaurant/reviews';
    final int restaurantId = widget.restaurant['restaurantId'];
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw', // Your token goes here
    };

    final request =
        http.Request('GET', Uri.parse('$url?restaurantId=$restaurantId'))
          ..headers.addAll(headers);

    final response = await request.send();

    if (response.statusCode == 200) {
      final responseBody = await response.stream.toBytes();
      final responseBodyString = utf8.decode(responseBody);
      final jsonResponse = json.decode(responseBodyString);
      setState(() {
        _reviews = List<Map<String, dynamic>>.from(jsonResponse['content']);
      });
    } else {
      print('Failed to load restaurant reviews');
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to load restaurant reviews')));
    }
  }

  void _toggleHoursExpanded() {
    setState(() {
      _isHoursExpanded = !_isHoursExpanded;
    });
  }

  void navigateToFullMenuPage() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (BuildContext context) => FullMenuPage(
          restaurantName: _restaurantDetails['name'] ?? 'Restaurant Name',
        ),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    fetchRestaurantDetails();
    fetchRestaurantReviews();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_restaurantDetails['name'] ?? 'Restaurant Name'),
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Images section
            // Replace with actual images
            Row(
              children: [
                Expanded(
                  child: GestureDetector(
                    onTap: () {
                      // Implement viewing larger image
                    },
                    child: Image.network(
                      'https://via.placeholder.com/100',
                      height: 100,
                      fit: BoxFit.cover,
                    ),
                  ),
                ),
                Expanded(
                  child: GestureDetector(
                    onTap: () {
                      // Implement viewing larger image
                    },
                    child: Image.network(
                      'https://via.placeholder.com/100',
                      height: 100,
                      fit: BoxFit.cover,
                    ),
                  ),
                ),
                Expanded(
                  child: GestureDetector(
                    onTap: () {
                      // Implement viewing larger image
                    },
                    child: Image.network(
                      'https://via.placeholder.com/100',
                      height: 100,
                      fit: BoxFit.cover,
                    ),
                  ),
                ),
              ],
            ),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 8.0),
              child: Text(
                _restaurantDetails['name'] ?? 'Restaurant Name',
                textAlign: TextAlign.center,
                style: const TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            ListTile(
              title: Text(
                _restaurantDetails['name'] ?? 'Restaurant Name',
                style:
                    const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              trailing: Text(
                '${_restaurantDetails['avgRating'] ?? 0.0} ★',
                style:
                    const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
            ),
            // Menu section
            // Replace with actual menu items
            GestureDetector(
              onTap: navigateToFullMenuPage,
              child: Container(
                padding: const EdgeInsets.all(8.0),
                decoration: BoxDecoration(
                  border: Border.all(color: Colors.grey),
                ),
                child: const Column(
                  children: [
                    Row(
                      children: [
                        Text('Menu Item 1'),
                        Spacer(),
                        Text('\$9.99'),
                      ],
                    ),
                    Row(
                      children: [
                        Text('Menu Item 2'),
                        Spacer(),
                        Text('\$5.99'),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            // Map section
            SizedBox(
              height: 200,
              child: (_restaurantDetails['lat'] != null &&
                      _restaurantDetails['lng'] != null)
                  ? LocationOnMap(
                      latitude: _restaurantDetails['lat'],
                      longitude: _restaurantDetails['lng'],
                      restaurantName:
                          _restaurantDetails['name'] ?? 'Unknown Restaurant',
                      address: _restaurantDetails['address_name'] ?? '',
                    )
                  : const Center(
                      child: Text(
                        'Map unavailable',
                        style: TextStyle(color: Colors.red),
                      ),
                    ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Opening Hour: ${_restaurantDetails['opening_hour'] ?? 'Not Available'}',
                    style: const TextStyle(
                      fontSize: 16,
                    ),
                  ),
                  Text(
                    'Closing Hour: ${_restaurantDetails['closing_hour'] ?? 'Not Available'}',
                    style: const TextStyle(
                      fontSize: 16,
                    ),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: _reviews.map((review) {
                  return GestureDetector(
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) =>
                              ReviewDetailPage(review: review),
                        ),
                      );
                    },
                    child: Card(
                      child: ListTile(
                          leading: CircleAvatar(
                              child: Text(review['rate'].toString())),
                          title: Text(review['title']),
                          subtitle: Text(review['content']),
                          isThreeLine: true,
                          trailing: IconButton(
                            icon: likedReviews.contains(review['id'])
                                ? const Icon(Icons.favorite, color: Colors.red)
                                : const Icon(Icons.favorite_border),
                            onPressed: () {
                              _likeReview(review['id'], memberId);
                            },
                          )),
                    ),
                  );
                }).toList(),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class FullMenuPage extends StatelessWidget {
  final String restaurantName;

  const FullMenuPage({super.key, required this.restaurantName});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(restaurantName)),
      // ... Full Menu implementation here ...
    );
  }
}
