package com.example.glasslivecaptioning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.glasslivecaptioning.GlassGestureDetector.Gesture;
import com.example.glasslivecaptioning.GlassGestureDetector.OnGestureListener;

import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity implements OnGestureListener {
    private GlassGestureDetector glassGestureDetector;
    private WebView myWebView;
    private boolean changingLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        glassGestureDetector = new GlassGestureDetector(this, this);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("https://ar-live-captioning.onrender.com/display");
        changingLanguage = false;
    }

    // hides system UI
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return glassGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        switch (gesture) {
            case SWIPE_DOWN:
                Log.d("App", "Swipe Down!");
                finish(); // closes out of app
                return true;
            case TAP:
                Log.d("App", "TAPPED!");
                // change language
                if (changingLanguage) {
                    myWebView.loadUrl("https://ar-live-captioning.onrender.com/display");
                } else {
                    myWebView.loadUrl("https://ar-live-captioning.onrender.com/changelanguage");
                }
                changingLanguage = !changingLanguage;

                return true;
            case SWIPE_FORWARD:
                Log.d("App", "swipe forward");
                KeyEvent clickRight = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
                myWebView.dispatchKeyEvent(clickRight);
                return true;
            case SWIPE_BACKWARD:
                Log.d("App", "swipe backward");
                KeyEvent clickLeft = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT);
                myWebView.dispatchKeyEvent(clickLeft);
                return true;
            case TWO_FINGER_SWIPE_FORWARD:
                Log.d("App", "double forward");
                return true;
            case TWO_FINGER_SWIPE_BACKWARD:
                Log.d("App", "double backward");
                return true;
            default:
                return false;
        }
    }
}