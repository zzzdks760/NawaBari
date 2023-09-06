import 'package:flutter/material.dart';
import 'package:new_nawabari/screen/login/main_login_screen.dart';
import 'alert_page.dart'; // Import your AlertPage class here
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart';

class SettingsPage extends StatelessWidget {
  const SettingsPage({super.key});

  // This function will be called when the user presses the logout button
  Future<void> _handleLogout(BuildContext context) async {
    try {
      await UserApi.instance.logout();
      Navigator.pushReplacement(
          context, MaterialPageRoute(builder: (context) => const LoginPage()));
      print('로그아웃 성공, SDK에서 토큰 삭제');
    } catch (error) {
      print('Logout failed: $error');
    }
  }

  // This function will be called when the user decides to unlink (delete) their account
  Future<void> _handleUnlink(BuildContext context) async {
    try {
      await UserApi.instance.unlink();
      print('연결 끊기 성공, SDK에서 토큰 삭제');
      Navigator.pushReplacement(
          context, MaterialPageRoute(builder: (context) => const LoginPage()));
    } catch (error) {
      print('Unlinking failed: $error');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Settings'),
      ),
      body: ListView(
        children: [
          ListTile(
            title: const Text('Notifications'),
            trailing: const Icon(Icons.navigate_next),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => AlertPage()), // Navigate to AlertPage
              );
            },
          ),
          ListTile(
            title: const Text('Notices'),
            trailing: const Icon(Icons.navigate_next),
            onTap: () {
              // Navigate to the Notices page
            },
          ),
          ListTile(
            title: const Text('Customer Inquiries'),
            trailing: const Icon(Icons.navigate_next),
            onTap: () {
              // Navigate to the Customer Inquiries page
            },
          ),
          ListTile(
            title: const Text('Policies'),
            trailing: const Icon(Icons.navigate_next),
            onTap: () {
              // Navigate to the Policies page
            },
          ),
          ListTile(
            title: const Text('Logout'),
            onTap: () => _handleLogout(context),
          ),
          ListTile(
            title: const Text('Withdraw'),
            onTap: () {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return AlertDialog(
                    title: const Text('Are you sure you want to withdraw?'),
                    content: const Text('This action is irreversible.'),
                    actions: [
                      TextButton(
                        child: const Text('No'),
                        onPressed: () {
                          Navigator.of(context).pop(); // Close the dialog
                        },
                      ),
                      TextButton(
                        child: const Text('Yes'),
                        onPressed: () async {
                          Navigator.of(context).pop(); // Close the dialog first
                          await _handleUnlink(context);
                        },
                      ),
                    ],
                  );
                },
              );
            },
          ),
        ],
      ),
    );
  }
}
