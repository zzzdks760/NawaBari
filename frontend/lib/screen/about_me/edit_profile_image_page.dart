import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:photo_gallery/photo_gallery.dart';
import 'package:permission_handler/permission_handler.dart';

class EditProfileImagePage extends StatefulWidget {
  @override
  _EditProfileImagePageState createState() => _EditProfileImagePageState();
}

class _EditProfileImagePageState extends State<EditProfileImagePage> {
  List<Medium> _images = [];
  final List<bool> _selectedImages = [];

  @override
  void initState() {
    super.initState();
    _loadImages();
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
        title: const Text('Edit Profile Image'),
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
                      _selectedImages[index] = !_selectedImages[index];
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
                        ),
                    ],
                  ),
                );
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextButton(
              onPressed: () {
                // Handle the 'Done' button press and save the selected image
              },
              child: const Text('Done'),
            ),
          ),
        ],
      ),
    );
  }
}
