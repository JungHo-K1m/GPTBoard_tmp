package com.example.gptboard;

import android.annotation.SuppressLint;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ImageView;

public class MyIMEService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{


    private KeyboardView keyboardView;
    private Keyboard keyboard;

    private ImageView companyLogo;
    private EditText editText;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateInputView() {
        View inputView = getLayoutInflater().inflate(R.layout.keyboard_view, null);

        keyboardView = inputView.findViewById(R.id.keyboard_view);
        keyboard = new Keyboard(this, R.xml.korean_keyboard);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);

        companyLogo = inputView.findViewById(R.id.company_logo);
        editText = inputView.findViewById(R.id.gpt_edit_text);

        // Set EditText to not focusable initially
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);

        // Set OnClickListener for the company logo
        companyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle EditText focusability
                boolean isFocusable = editText.isFocusable();
                editText.setFocusable(!isFocusable);
                editText.setFocusableInTouchMode(!isFocusable);
                if (!isFocusable) {
                    editText.requestFocus();
                } else {
                    editText.clearFocus();
                }
            }
        });

        return inputView;
    }
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection == null) {
            return;
        }

        // Check if the EditText is activated (focusable)
        if (editText.isFocusable()) {
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    CharSequence selectedText = inputConnection.getSelectedText(0);
                    if (selectedText != null && selectedText.length() > 0) {
                        inputConnection.commitText("", 1);
                    } else {
                        inputConnection.deleteSurroundingText(1, 0);
                    }
                    updateEditText();
                    break;
                case Keyboard.KEYCODE_DONE:
                    inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                    break;
                default:
                    char code = (char) primaryCode;
                    inputConnection.commitText(String.valueOf(code), 1);
                    updateEditText();
                    break;
            }
        }
    }

    private void updateEditText() {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection == null) {
            return;
        }

        CharSequence textBeforeCursor = inputConnection.getTextBeforeCursor(1000, 0);
        CharSequence textAfterCursor = inputConnection.getTextAfterCursor(1000, 0);
        editText.setText(textBeforeCursor.toString() + textAfterCursor.toString());
        editText.setSelection(textBeforeCursor.length());

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
