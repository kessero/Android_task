package pl.hekko.kesser.dominikkesselring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/*
 * Created by kesser on 21.01.16.
 */
public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIME = 5000;
    private static final int PREFERENCE_MODE_PRIVATE = 1;
    private Handler handler;
    private Runnable runnable;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", PREFERENCE_MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("login", false);
        startIntentAfterPeriodTime();
    }

    private void startIntentAfterPeriodTime() {
        handler = new Handler();
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





