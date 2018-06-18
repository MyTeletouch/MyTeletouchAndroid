package com.madeit.julian.myteletouch;

import com.madeit.julian.myteletouch.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class KeyboardActivity extends Activity {
    private ImageButton mouseButton;
    private ImageButton gamepadButton;
    private EditText dummyEdit;
    private byte[] keysEmpty = new byte[3];

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_keyboard);
        addListenerOnButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(dummyEdit, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    public void addListenerOnButton() {
        final Context context = this;

        gamepadButton = (ImageButton) findViewById(R.id.joystickButton);
        gamepadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent gamepadActivity = new Intent(context, GamepadActivity.class);
                gamepadActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(gamepadActivity);
            }

        });

        mouseButton = (ImageButton) findViewById(R.id.mouseButton);
        mouseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent mouseActivity = new Intent(context, MouseActivity.class);
                mouseActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mouseActivity);
            }

        });

        dummyEdit = (EditText) this.findViewById(R.id.dummyEdit);
        dummyEdit.setTextColor(Color.TRANSPARENT);
        dummyEdit.setHintTextColor(Color.TRANSPARENT);
        dummyEdit.setLinkTextColor(Color.TRANSPARENT);
        dummyEdit.setBackgroundColor(Color.TRANSPARENT);
        dummyEdit.setAlpha(0);
        dummyEdit.setText(R.string.empty_text);
        dummyEdit.setSelection(dummyEdit.length());
        dummyEdit.requestFocus();
        dummyEdit.addTextChangedListener(new TextWatcher() {
            private int lenBefore;

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lenBefore = s.length();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lenBefore > s.length() || s.length() == 0)
                    try {
                        processScanCode(AnsiiToScanCodeConverter.scanCode(8));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                else for (int i = s.length() - 1; i < s.length(); i++) {
                    try {
                        processScanCode(AnsiiToScanCodeConverter.scanCodeFromChar(s.charAt(i)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void processScanCode(ScanCodeInfo info) throws InterruptedException {
        byte[] keys = new byte[3];
        keys[0] = info.SpecialKeys;
        keys[1] = 0;
        keys[2] = info.ScanCode;

        MainController.getMainController().setKeys(keys);
        Thread.sleep(50);
        MainController.getMainController().setKeys(keysEmpty);
        Thread.sleep(50);
        MainController.getMainController().setKeys(keysEmpty);
    }
}
