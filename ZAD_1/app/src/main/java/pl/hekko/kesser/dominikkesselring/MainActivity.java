package pl.hekko.kesser.dominikkesselring;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final String VIDEO_URL = "https://www.youtube.com/watch?v=M6wRnouGZFQ";
    private final String FULL_SCREEN = "force_fullscreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button otherOption = (Button) findViewById(R.id.buttonOtherOption);
        otherOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(VIDEO_URL));
                intent.putExtra(FULL_SCREEN, true);
                startActivity(intent);
            }
        });

    }
}
