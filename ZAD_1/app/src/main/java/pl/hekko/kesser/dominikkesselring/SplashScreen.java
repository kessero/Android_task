package pl.hekko.kesser.dominikkesselring;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/*
 * Created by kesser on 21.01.16.
 */
public class SplashScreen extends AppCompatActivity {

    private static final String LOGIN_KEY = "login";
    private static final String PREFS_F = "MyPrefs";
    private static final int SPLASH_TIME = 5000;
    private static Handler handler;
    private static Runnable runnable;
    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PREFS_F, Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean(LOGIN_KEY, false);
        startIntentAfterPeriodTime();
    }

    private void startIntentAfterPeriodTime() {
        handler = new Handler();
        //TODO Weak/reference
        runnable = new MyRunnable();

        handler.postDelayed(runnable, SPLASH_TIME);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, SPLASH_TIME);

    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {

            if (!isLogin) {
                startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }
    }
}





