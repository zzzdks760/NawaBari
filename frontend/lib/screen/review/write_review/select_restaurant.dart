import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:new_nawabari/screen/review/write_review/one_line_review.dart';
import 'package:new_nawabari/widget/utils.dart';

class SelectRestaurant extends StatefulWidget {
  const SelectRestaurant({super.key});

  @override
  _SelectRestaurantState createState() => _SelectRestaurantState();
}

class _SelectRestaurantState extends State<SelectRestaurant> {
  List<Map<String, dynamic>> restaurants = [];
  String searchQuery = "";
  List<int> zoneIds = [];

  @override
  void initState() {
    super.initState();
    getZoneIds().then((fetchedZoneIds) {
      setState(() {
        zoneIds = fetchedZoneIds;
      });
    });
  }

  void _navigateToOneLineReview(Map<String, dynamic> restaurant) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => OneLineReview(restaurant: restaurant),
      ),
    );
  }

  Future<void> _searchRestaurants(String keyword) async {
    const String url = 'http://43.202.82.108:8080/api/v1/restaurants/search';
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw', // Replace with your token
    };
    final response = await http.get(
      Uri.parse('$url?keyword=$keyword'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      final jsonResponse = json.decode(utf8.decode(response.bodyBytes));
      setState(() {
        restaurants = (jsonResponse['content'] as List)
            .map((item) => {
                  'name': item['name'],
                  'location': item['address_name'],
                  'restaurantId': item['restaurantId'],
                  'zoneId': item['zoneId'],
                })
            .toList();
      });
    } else {
      // Handle error response or other status codes as needed.
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Write a review'),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    decoration: const InputDecoration(
                      labelText: 'Search',
                      border: OutlineInputBorder(),
                    ),
                    onChanged: (value) {
                      searchQuery = value;
                    },
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.search),
                  onPressed: () {
                    _searchRestaurants(searchQuery);
                  },
                )
              ],
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: restaurants.length,
              itemBuilder: (BuildContext context, int index) {
                return InkWell(
                  onTap: zoneIds.contains(restaurants[index]['zoneId'])
                      ? () => _navigateToOneLineReview(restaurants[index])
                      : null, // Disable onTap for grey-colored items.
                  child: Card(
                    color: zoneIds.contains(restaurants[index]['zoneId'])
                        ? Colors.white
                        : Colors.grey[
                            300], // <-- Added this line to check the color
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            restaurants[index]['name'],
                            style: const TextStyle(
                              fontSize: 18, // <-- Removed semicolon
                              fontWeight:
                                  FontWeight.bold, // <-- Removed semicolon
                            ),
                          ),
                          Text(
                            restaurants[index]['location'],
                            style: TextStyle(
                              fontSize: 16, // <-- Removed semicolon
                              color: Colors.grey[600], // <-- Removed semicolon
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
