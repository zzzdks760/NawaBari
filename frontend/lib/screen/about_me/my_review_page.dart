import 'dart:io';
import 'package:flutter/material.dart';

class MyReviewPage extends StatelessWidget {
  // This is a sample list of reviews with dummy data.
  // Replace this with actual data fetched from your server or database.
  // Each review has a restaurant name, review date, review text, and a list of image file paths.
  final List<dynamic> myReviews;

  const MyReviewPage({super.key, required this.myReviews});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('My Reviews'),
      ),
      body: ListView.builder(
        itemCount: myReviews.length,
        itemBuilder: (BuildContext context, int index) {
          final review = myReviews[index];
          return Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  '${review['restaurantName']} - ${review['reviewDate']}',
                  style: const TextStyle(
                      fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  review['reviewText'],
                  textAlign: TextAlign.left,
                  style: const TextStyle(
                    fontSize: 16,
                  ),
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  for (final imagePath in review['images'])
                    SizedBox(
                      width: MediaQuery.of(context).size.width / 3.3,
                      height: MediaQuery.of(context).size.width / 3.3,
                      child: imagePath.isNotEmpty
                          ? Image.file(
                              File(imagePath),
                              fit: BoxFit.cover,
                            )
                          : Image.network(
                              // Add your dummy image URL here
                              'https://via.placeholder.com/100',
                              fit: BoxFit.cover,
                            ),
                    ),
                ],
              ),
              const Divider(),
            ],
          );
        },
      ),
    );
  }
}
