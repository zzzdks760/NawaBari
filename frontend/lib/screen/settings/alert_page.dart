import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class AlertPage extends StatefulWidget {
  @override
  _AlertPageState createState() => _AlertPageState();
}

class _AlertPageState extends State<AlertPage> {
  bool _deviceNotificationStatus = false;

  Future<void> _openAppNotificationSettings() async {
    const settingsUrl = 'app-settings:';
    if (await canLaunchUrl(Uri.parse(settingsUrl))) {
      await launchUrl(Uri.parse(settingsUrl));
    } else {
      print('Could not open app notification settings');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Notifications'),
      ),
      body: Column(
        children: [
          ListTile(
            title: const Text('Device notifications'),
            trailing: Switch(
              value: _deviceNotificationStatus,
              onChanged: (bool value) {
                setState(() {
                  _deviceNotificationStatus = value;
                });
                _openAppNotificationSettings();
              },
            ),
          ),
        ],
      ),
    );
  }
}
