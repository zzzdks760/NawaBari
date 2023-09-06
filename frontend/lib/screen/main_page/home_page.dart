import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:new_nawabari/screen/about_me/my_info_page.dart';
import 'package:new_nawabari/screen/restaurant/detail_page.dart';
import 'package:new_nawabari/screen/restaurant/search_restaurant.dart';
import 'package:new_nawabari/screen/review/write_review/select_restaurant.dart';
import 'package:new_nawabari/widget/utils.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:location/location.dart';

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  String currentRegion = 'Region 1';
  List<int>? storedZoneIds;
  int currentPage = 0;
  int pageSize = 10;
  bool isLoadingMore = false;
  List<Map<String, dynamic>> allRestaurants = [];
  final ScrollController _scrollController = ScrollController();
  bool isInitialDataLoaded = false;
  bool isLastPage = false;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _scrollController.addListener(() {
      if (_scrollController.position.atEdge) {
        if (_scrollController.position.pixels != 0) {
          _loadMoreData();
        }
      }
    });
  }

  void _loadInitialData() async {
    try {
      var restaurants = await fetchRestaurants();
      setState(() {
        allRestaurants = restaurants;
      });
    } catch (e) {
      // Handle error here, if needed.
    }
  }

  void _loadMoreData() async {
    if (!isLoadingMore && !isLastPage) {
      setState(() {
        isLoadingMore = true;
        currentPage++;
      });

      var newRestaurants = await fetchRestaurants();

      setState(() {
        allRestaurants.addAll(newRestaurants);
        isLoadingMore = false;
      });
    }
  }

  Future<LocationData?> getCurrentLocation() async {
    Location location = Location();
    bool serviceEnabled;
    PermissionStatus permissionGranted;

    serviceEnabled = await location.serviceEnabled();
    if (!serviceEnabled) {
      serviceEnabled = await location.requestService();
      if (!serviceEnabled) {
        return null;
      }
    }

    permissionGranted = await location.hasPermission();
    if (permissionGranted == PermissionStatus.denied) {
      permissionGranted = await location.requestPermission();
      if (permissionGranted != PermissionStatus.granted) {
        return null;
      }
    }

    return await location.getLocation();
  }

  Future<void> fetchAndStoreMyInfo() async {
    const String url = 'http://43.202.82.108:8080/api/v1/MyPage?id=22';

    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw',
    };

    final response = await http.get(Uri.parse(url), headers: headers);

    if (response.statusCode == 200) {
      final jsonResponse = json.decode(response.body);
      List<int> zoneIds = (jsonResponse['zoneIds'] as List).cast<int>();
      // Store zoneIds in SharedPreferences
      await storeZoneIds(zoneIds);
    } else {
      throw Exception('Failed to fetch personal information');
    }
  }

  Future<void> storeZoneIds(List<int> zoneIds) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setStringList(
        'zoneIds', zoneIds.map((id) => id.toString()).toList());
  }

  Future<List<Map<String, dynamic>>> fetchRestaurants() async {
    String url =
        'http://43.202.82.108:8080/api/v1/location/restaurants?page=$currentPage&size=$pageSize';

    // Fetch current location
    LocationData? currentLocation = await getCurrentLocation();
    if (currentLocation == null) {
      throw Exception('Failed to fetch current location');
    }

    // String? token = await _fetchTokenFromLocal();
    // if (token == null) {
    //   throw Exception('Token not found!');
    // }

    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Authorization':
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTY5MzQ1Nzk1NCwiZW1haWwiOiJjaG9pc2IzNjMxQGdtYWlsLmNvbSJ9.iEBj9EtVQoXDzWV3cAJo8pxyeZ2JBA-eEahN1h7yqC5OG9NTCcoI3gjUqvAdXP06-WJ_5xqw_2_wuqMmJPrvOw',
      // 'Bearer $token',
    };
    final Map<String, double> body = {
      'current_lat': currentLocation.latitude!,
      'current_lng': currentLocation.longitude!,
    };
    final response = await http.post(
      Uri.parse(url),
      headers: headers,
      body: json.encode(body),
    );

    if (response.statusCode == 200) {
      final jsonResponse = json.decode(utf8.decode(response.bodyBytes));
      if (jsonResponse["last"] == true) {
        setState(() {
          isLastPage = true;
        });
      }
      List<Map<String, dynamic>> restaurants = (jsonResponse['content'] as List)
          .map((item) => item as Map<String, dynamic>)
          .toList();
      return restaurants;
    } else {
      throw Exception('Failed to load restaurants');
    }
  }

  void navigateToWriteReviewPage() {
    Navigator.push(
        context,
        MaterialPageRoute(
          builder: (BuildContext context) => const SelectRestaurant(),
        ));
  }

  void navigateToRestaurantDetailPage(restaurantList, int index) {
    // Assuming restaurantList is properly set
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (BuildContext context) => DetailPage(
            restaurant:
                restaurantList[index]), // Pass the selected restaurant data
      ),
    );
  }

  Widget _buildRestaurantListView() {
    return Column(
      children: [
        const SizedBox(height: 20),
        Expanded(
          child: ListView.builder(
            controller: _scrollController,
            itemCount: isLoadingMore
                ? allRestaurants.length + 1
                : allRestaurants.length,
            itemBuilder: (BuildContext context, int index) {
              if (index == allRestaurants.length) {
                return const Center(child: CircularProgressIndicator());
              }
              return InkWell(
                onTap: () =>
                    navigateToRestaurantDetailPage(allRestaurants, index),
                child: Card(
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Expanded(
                        flex: 1,
                        child: Image.network(
                          'https://via.placeholder.com/150',
                          fit: BoxFit.cover,
                          height:
                              100, // You can adjust this to fit your design.
                        ),
                      ),
                      Expanded(
                        flex: 2,
                        child: Padding(
                          padding: const EdgeInsets.all(5.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                allRestaurants[index]['name'],
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  fontSize: 16,
                                ),
                              ),
                              Text(
                                '${allRestaurants[index]['avgRating']} ★ (${allRestaurants[index]['reviewCount']} reviews)',
                                style: TextStyle(
                                  fontSize: 14,
                                  color: Colors.grey[600],
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: GestureDetector(
          onTap: () {
            // Implement logic to change the region when clicked
          },
          child: Text(currentRegion),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.search),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => const SearchRestaurant()),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.person),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const MyInfoPage()),
              );
            },
          ),
        ],
      ),
      body: _buildRestaurantListView(),
      floatingActionButton: FloatingActionButton(
        onPressed: navigateToWriteReviewPage,
        backgroundColor: Theme.of(context).colorScheme.secondary,
        child: const Icon(Icons.add),
      ),
    );
  }
}
