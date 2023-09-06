import 'package:flutter/material.dart';
import 'package:new_nawabari/screen/review/write_review/select_image.dart';
import 'preview.dart';

class RatingPage extends StatefulWidget {
  final String review;
  final Map<String, dynamic> restaurant;

  const RatingPage({Key? key, required this.review, required this.restaurant})
      : super(key: key);

  @override
  _RatingPageState createState() => _RatingPageState();
}

class _RatingPageState extends State<RatingPage> {
  double _rating = 3.0;

  void _navigateToPreviewPage() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => PreviewPage(
          review: widget.review,
          restaurant: widget.restaurant,
          rating: _rating,
          selectedImages: const [], // Passing an empty list for now
        ),
      ),
    );
  }

  void _navigateToSelectImagePage() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => SelectImage(
          review: widget.review,
          restaurant: widget.restaurant,
          rating: _rating,
          selectedImages: const [], // Passing an empty list for now
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('리뷰 쓰기[${widget.restaurant['name']}]'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              '이 식당의 평점을 매겨주세요.',
              style: TextStyle(fontSize: 18),
            ),
            Slider(
              value: _rating,
              onChanged: (newRating) {
                setState(() {
                  _rating = newRating;
                });
              },
              divisions: 4,
              min: 1.0,
              max: 5.0,
            ),
            Text('Rating: $_rating/5'),
            ElevatedButton(
              onPressed: _navigateToSelectImagePage,
              child: const Text('미리보기'),
            ),
          ],
        ),
      ),
    );
  }
}
