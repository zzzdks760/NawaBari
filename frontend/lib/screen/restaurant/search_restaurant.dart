import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:new_nawabari/screen/restaurant/detail_page.dart';
import 'dart:convert';
import 'package:new_nawabari/widget/utils.dart';

class SearchRestaurant extends StatefulWidget {
  const SearchRestaurant({super.key});

  @override
  _SearchRestaurantState createState() => _SearchRestaurantState();
}

class _SearchRestaurantState extends State<SearchRestaurant> {
  List<Map<String, dynamic>> restaurants = [];
  String searchQuery = "";
  List<int> zoneIds = [];
  int currentPage = 0;
  int pageSize =
      10; // Number of items per page (you can change it as per your need)
  bool isLoadingMore = false;
  bool isLastPage = false;
  final ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    getZoneIds().then((fetchedZoneIds) {
      setState(() {
        zoneIds = fetchedZoneIds;
      });
    });
  }

  void _navigateToDetailPage(Map<String, dynamic> restaurant) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => DetailPage(restaurant: restaurant),
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
      Uri.parse('$url?keyword=$keyword&page=$currentPage&size=$pageSize'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      final jsonResponse = json.decode(utf8.decode(response.bodyBytes));
      setState(() {
        if (currentPage == 0) {
          restaurants = (jsonResponse['content'] as List)
              .map((item) => {
                    'name': item['name'],
                    'location': item['address_name'],
                    'restaurantId': item['restaurantId'],
                    'zoneId': item['zoneId'],
                  })
              .toList();
        } else {
          restaurants.addAll((jsonResponse['content'] as List)
              .map((item) => {
                    'name': item['name'],
                    'location': item['address_name'],
                    'restaurantId': item['restaurantId'],
                    'zoneId': item['zoneId'],
                  })
              .toList());
        }

        if (jsonResponse["last"] == true) {
          isLastPage = true;
        }
      });
      isLoadingMore = false;
    } else {
      // Handle error response or other status codes as needed.
      isLoadingMore = false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Search a Restaurant'),
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
                  onTap: () => _navigateToDetailPage(restaurants[index]),
                  child: Card(
                    color: Colors.white, // 여기서 카드의 색상을 항상 하얀색으로 설정
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            restaurants[index]['name'],
                            style: const TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          Text(
                            restaurants[index]['location'],
                            style: TextStyle(
                              fontSize: 16,
                              color: Colors.grey[600],
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
