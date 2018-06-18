package com.madeit.julian.myteletouch;

import com.madeit.julian.myteletouch.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class GamepadActivity extends Activity {
    ImageButton keyboardButton;
    ImageButton mouseButton;
    RingView ringView;
    RingButtonView button1;
    RingButtonView button2;
    RingButtonView button3;
    RingButtonView button4;
    RingButtonView button5;

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
        setContentView(R.layout.activity_gamepad);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;

        keyboardButton = (ImageButton) findViewById(R.id.keyboardButton);
        keyboardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent keyboardActivity = new Intent(context, KeyboardActivity.class);
                keyboardActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(keyboardActivity);
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

        ringView = (RingView) findViewById(R.id.ringView);
        ringView.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {

            }
        });

        ringView.setOnOffsetChangeListener(new RingView.OnOffsetChangeListener() {
            @Override
            public void onOffsetChanged(View v, Point offset) {
                MainController.getMainController().setJoystickPosition(offset);
            }
        });

        button1 = (RingButtonView) findViewById(R.id.button1);
        button1.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsJoystick1Down(isDown);
            }
        });

        button2 = (RingButtonView) findViewById(R.id.button2);
        button2.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsJoystick2Down(isDown);
            }
        });

        button3 = (RingButtonView) findViewById(R.id.button3);
        button3.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsJoystick3Down(isDown);
            }
        });

        button4 = (RingButtonView) findViewById(R.id.button4);
        button4.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsJoystick4Down(isDown);
            }
        });

        button5 = (RingButtonView) findViewById(R.id.button5);
        button5.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsJoystick5Down(isDown);
            }
        });
    }
}
