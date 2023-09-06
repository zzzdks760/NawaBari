import 'package:flutter/material.dart';
import 'preview.dart'; // Change this import to 'preview.dart'
import 'rating_page.dart';

class OneLineReview extends StatefulWidget {
  final Map<String, dynamic> restaurant;

  const OneLineReview({Key? key, required this.restaurant}) : super(key: key);

  @override
  _OneLineReviewState createState() => _OneLineReviewState();
}

class _OneLineReviewState extends State<OneLineReview> {
  final TextEditingController _reviewController = TextEditingController();

  void _navigateToRatingPage() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => RatingPage(
          review: _reviewController.text,
          restaurant: widget.restaurant,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Write a Review'),
      ),
      body: Column(
        children: [
          Expanded(
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                      'Please write a review about the restaurant',
                      style: TextStyle(fontSize: 18),
                      textAlign: TextAlign.center,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      controller: _reviewController,
                      maxLength: 30,
                      decoration: const InputDecoration(
                        hintText: 'Write a one-line review',
                        border: OutlineInputBorder(),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
      bottomNavigationBar: Padding(
        padding: const EdgeInsets.all(8.0),
        child: ElevatedButton(
          onPressed: _navigateToRatingPage, // Updated to navigate to RatingPage
          child: const Text('Next'),
        ),
      ),
    );
  }
}
