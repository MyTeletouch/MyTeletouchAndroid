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
public class MouseActivity extends Activity {
    ImageButton keyboardButton;
    ImageButton gamepadButton;
    RingView ringView;
    RectButtonView leftButton;
    RectButtonView middleButton;
    RectButtonView rightButton;

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
        setContentView(R.layout.activity_mouse);
        addListenerOnButton();
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

        keyboardButton = (ImageButton) findViewById(R.id.keyboardButton);
        keyboardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent keyboardActivity = new Intent(context, KeyboardActivity.class);
                keyboardActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(keyboardActivity);
            }

        });

        ringView = (RingView) findViewById(R.id.ringView);
        ringView.setIsMouse(true);
        ringView.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsMouseLeftDown(isDown);
            }
        });

        ringView.setOnOffsetChangeListener(new RingView.OnOffsetChangeListener() {
            @Override
            public void onOffsetChanged(View v, Point offset) {
                MainController.getMainController().setMousePosition(offset);
            }
        });

        leftButton = (RectButtonView) findViewById(R.id.leftButton);
        leftButton.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsMouseLeftDown(isDown);
            }
        });

        middleButton = (RectButtonView) findViewById(R.id.middleButton);
        middleButton.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsMouseMiddleDown(isDown);
            }
        });

        rightButton = (RectButtonView) findViewById(R.id.rightButton);
        rightButton.setIsDownChangeListener(new OnIsDownChangeListener() {
            @Override
            public void onIsDownChanged(View v, boolean isDown) {
                MainController.getMainController().setIsMouseRightDown(isDown);
            }
        });
    }
}
