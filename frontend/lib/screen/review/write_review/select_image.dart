import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:new_nawabari/screen/review/write_review/preview.dart';
import 'package:photo_gallery/photo_gallery.dart';
import 'package:permission_handler/permission_handler.dart';

class SelectImage extends StatefulWidget {
  final String review;
  final List<Uint8List> selectedImages;
  final Map<String, dynamic> restaurant;
  final double rating;

  const SelectImage(
      {super.key,
      required this.review,
      required this.restaurant,
      required this.rating,
      required this.selectedImages});

  @override
  _SelectImageState createState() => _SelectImageState();
}

class _SelectImageState extends State<SelectImage> {
  List<Medium> _images = [];
  final List<bool> _selectedImages = [];
  int _selectedCount = 0;

  @override
  void initState() {
    super.initState();
    _loadImages();
  }

  void _navigateToPreviewPage(List<Uint8List> selectedImagesData) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => PreviewPage(
          review: widget.review,
          restaurant: widget.restaurant,
          rating: widget.rating,
          selectedImages: selectedImagesData,
        ),
      ),
    );
  }

  Future<void> _loadImages() async {
    final status = await Permission.photos.request();
    if (status.isGranted) {
      final albums = await PhotoGallery.listAlbums(
        mediumType: MediumType.image,
      );
      if (albums.isNotEmpty) {
        final media = await albums.first.listMedia();
        setState(() {
          _images = media.items;
          _selectedImages
              .addAll(List.generate(_images.length, (index) => false));
        });
      }
    } else {
      _requestPermissionAndLoadImages();
    }
  }

  Future<void> _requestPermissionAndLoadImages() async {
    final status = await Permission.photos.request();
    if (status.isGranted) {
      _loadImages();
    } else {
      // You can show a message to the user to inform them that permission is needed.
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
          Expanded(
            child: GridView.builder(
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3,
                crossAxisSpacing: 4.0,
                mainAxisSpacing: 4.0,
              ),
              itemCount: _images.length,
              itemBuilder: (BuildContext context, int index) {
                return GestureDetector(
                  onTap: () {
                    setState(() {
                      if (!_selectedImages[index] && _selectedCount < 3) {
                        _selectedImages[index] = true;
                        _selectedCount++;
                      } else if (_selectedImages[index]) {
                        _selectedImages[index] = false;
                        _selectedCount--;
                      }
                    });
                  },
                  child: Stack(
                    children: [
                      FutureBuilder<List<int>>(
                        future: _images[index].getThumbnail(highQuality: true),
                        builder: (BuildContext context,
                            AsyncSnapshot<List<int>> snapshot) {
                          if (snapshot.connectionState ==
                              ConnectionState.done) {
                            if (snapshot.hasData) {
                              return Image.memory(
                                Uint8List.fromList(snapshot.data!),
                                fit: BoxFit.cover,
                                width: double.infinity,
                                height: double.infinity,
                              );
                            } else {
                              return const Center(
                                  child: Text('Error loading thumbnail'));
                            }
                          } else {
                            return const Center(
                                child: CircularProgressIndicator());
                          }
                        },
                      ),
                      if (_selectedImages[index])
                        Container(
                          decoration: BoxDecoration(
                            border: Border.all(color: Colors.blue, width: 2),
                          ),
                          child: Align(
                            alignment: Alignment.topRight,
                            child: Padding(
                              padding: const EdgeInsets.all(2.0),
                              child: CircleAvatar(
                                radius: 10,
                                backgroundColor: Colors.blue,
                                child: Text(
                                  (_selectedImages
                                              .sublist(0, index)
                                              .where((selected) => selected)
                                              .length +
                                          1)
                                      .toString(),
                                  style: const TextStyle(
                                    fontSize: 12,
                                    color: Colors.white,
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ),
                    ],
                  ),
                );
              },
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              TextButton(
                onPressed: () async {
                  List<Uint8List> selectedImagesData = [];
                  for (int i = 0; i < _images.length; i++) {
                    if (_selectedImages[i]) {
                      List<int> imageData =
                          await _images[i].getThumbnail(highQuality: true);
                      selectedImagesData.add(Uint8List.fromList(imageData));
                    }
                  }

                  _navigateToPreviewPage(selectedImagesData);
                },
                child: const Text('Preview'),
              ),
              TextButton(
                onPressed: () {
                  // Navigate to the next page
                },
                child: Text('Next ($_selectedCount/3)'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
