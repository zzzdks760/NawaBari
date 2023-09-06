import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

class LocationOnMap extends StatefulWidget {
  final double latitude;
  final double longitude;
  final String restaurantName;
  final String address; // <-- 주소 속성 추가

  const LocationOnMap({
    required this.latitude,
    required this.longitude,
    required this.restaurantName,
    required this.address, // <-- 생성자에 주소 추가
    Key? key,
  }) : super(key: key);

  @override
  _LocationOnMapState createState() => _LocationOnMapState();
}

class _LocationOnMapState extends State<LocationOnMap> {
  late GoogleMapController _controller;

  @override
  Widget build(BuildContext context) {
    final LatLng position = LatLng(widget.latitude, widget.longitude);

    return GoogleMap(
      initialCameraPosition: CameraPosition(
        target: position,
        zoom: 15.0, // Adjust zoom level as desired.
      ),
      markers: {
        Marker(
          markerId: MarkerId(widget.restaurantName),
          position: position,
          infoWindow: InfoWindow(
            title: widget.restaurantName,
            snippet: widget.address, // <-- 주소를 마커에 추가
          ),
        ),
      },
      onMapCreated: (GoogleMapController controller) {
        _controller = controller;
      },
    );
  }
}
