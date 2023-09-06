import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

Future<String?> _fetchTokenFromLocal() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  return prefs.getString('access_token');
}

Future<List<int>> getZoneIds() async {
  final prefs = await SharedPreferences.getInstance();
  // Get the list of strings and then convert them back to integers
  List<String>? storedIds = prefs.getStringList('zoneIds');
  if (storedIds != null && storedIds.isNotEmpty) {
    return storedIds.map((id) => int.parse(id)).toList();
  }
  return [];
}
