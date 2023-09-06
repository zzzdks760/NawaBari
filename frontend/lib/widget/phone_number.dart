import 'package:flutter/services.dart';

class PhoneInputFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(
      TextEditingValue oldValue, TextEditingValue newValue) {
    if (oldValue.text.length > newValue.text.length) {
      // Handle deletion
      return newValue;
    }

    final StringBuffer newText = StringBuffer();
    int j = 0;
    for (int i = 0; i < newValue.text.length && j < 11; i++) {
      if (newValue.text[i].isNotEmpty &&
          RegExp(r'\d').hasMatch(newValue.text[i])) {
        newText.write(newValue.text[i]);
        if ((j == 2) | (j == 6)) {
          newText.write('-');
        }
        j++;
      }
    }
    return newValue.copyWith(
        text: newText.toString(),
        selection: TextSelection.collapsed(offset: newText.length));
  }
}
