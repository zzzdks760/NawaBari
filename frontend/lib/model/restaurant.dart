import 'dart:core';

import 'package:new_nawabari/model/location.dart';
import 'package:new_nawabari/model/time_of_day.dart';

class Restaurant {
  int id;
  String name;
  String? imagePath;
  int? phoneNumber;
  Location? location; // To be created later
  TimeOfDay? openTime;
  TimeOfDay? closeTime;
  String? foodCategory;
  int? numberOfReviews;
  double? averageRating;
  DateTime? creationTime;
  DateTime? updateTime;

  Restaurant({
    required this.id,
    required this.name,
    this.imagePath,
    this.phoneNumber,
    this.location,
    this.openTime,
    this.closeTime,
    this.foodCategory,
    this.numberOfReviews,
    this.averageRating,
    this.creationTime,
    this.updateTime,
  });
}
