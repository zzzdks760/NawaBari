import 'package:flutter/material.dart';

class EditPhoneNumberPage extends StatefulWidget {
  final String currentPhoneNumber;

  const EditPhoneNumberPage({required this.currentPhoneNumber});

  @override
  _EditPhoneNumberPageState createState() => _EditPhoneNumberPageState();
}

class _EditPhoneNumberPageState extends State<EditPhoneNumberPage> {
  final TextEditingController _phoneNumberController = TextEditingController();
  final TextEditingController _verificationCodeController =
      TextEditingController();
  bool _verificationCodeVisible = false;

  void _sendAuthenticationNumber() {
    setState(() {
      _verificationCodeVisible = true;
    });
  }

  void _confirmAuthenticationNumber() {
    // Handle authentication number confirmation logic here
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Edit phone number'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            TextField(
              controller: _phoneNumberController,
              keyboardType: TextInputType.phone,
              decoration: InputDecoration(
                hintText: widget.currentPhoneNumber,
              ),
            ),
            const SizedBox(height: 16),
            if (!_verificationCodeVisible)
              Center(
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                    color: Theme.of(context).primaryColor,
                  ),
                  child: TextButton(
                    onPressed: _sendAuthenticationNumber,
                    child: const Text(
                      'Send authentication number',
                      style: TextStyle(color: Colors.white),
                    ),
                  ),
                ),
              ),
            if (_verificationCodeVisible)
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  TextField(
                    controller: _verificationCodeController,
                    keyboardType: TextInputType.number,
                    maxLength: 6,
                    decoration: const InputDecoration(
                      labelText: 'Verification code',
                    ),
                  ),
                  const SizedBox(height: 16),
                  Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      color: Theme.of(context).primaryColor,
                    ),
                    child: TextButton(
                      onPressed: _confirmAuthenticationNumber,
                      child: const Text(
                        'Confirm authentication number',
                        style: TextStyle(color: Colors.white),
                      ),
                    ),
                  ),
                ],
              ),
          ],
        ),
      ),
    );
  }
}
