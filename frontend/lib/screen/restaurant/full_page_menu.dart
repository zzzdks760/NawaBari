import 'package:flutter/material.dart';

class FullMenuPage extends StatefulWidget {
  final String restaurantName;

  const FullMenuPage({required this.restaurantName});

  @override
  _FullMenuPageState createState() => _FullMenuPageState();
}

class _FullMenuPageState extends State<FullMenuPage> {
  List<String> menuItems = [
    'Menu item 1',
    'Menu item 2',
    'Menu item 3',
    // ... add more menu items
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.restaurantName),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Menu Details',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 10),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: menuItems.map((menuItem) {
                return Padding(
                  padding: const EdgeInsets.symmetric(vertical: 4.0),
                  child: Text(menuItem, style: const TextStyle(fontSize: 16)),
                );
              }).toList(),
            ),
          ],
        ),
      ),
    );
  }
}
