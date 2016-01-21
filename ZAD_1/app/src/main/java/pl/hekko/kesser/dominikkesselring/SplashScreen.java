package pl.hekko.kesser.dominikkesselring;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/*
 * Created by kesser on 21.01.16.
 */
public class SplashScreen extends AppCompatActivity {


    private static final int SPLASH_TIME = 5000;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startIntentAfterPeriodTime();
    }



    private void startIntentAfterPeriodTime() {

        handler = new Handler();
        runnable = new Runnable() {

            public void run() {

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}



