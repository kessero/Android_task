package pl.hekko.kesser.dominikkesselring;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final static String BASE_SERVER_URL = "http://www.kesser.hekko.pl/Prog/ZAD/";
    private final String ERROR_GET_DATA_FROM_SERVER = "error";
    private final static String PREFS_F = "MyPrefs";
    private static final String TAG_ARRAY = "array";
    static final String TAG_TITLE = "title";
    static final String TAG_DESC = "desc";
    static final String TAG_URL = "url";
    private final static String PAGE_0 = "page_0.json";
    private final static String PAGE = "page_";
    private final static String PAGE_END_FILE = ".json";
    private final static int MAX_PAGE_NUMBER = 2;
    private ProgressBar progressBar = null;
    private ArrayList<HashMap<String, String>> dataList;
    private int licznik;
    private LazyAdapter adapter;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        gridView = (GridView) findViewById(R.id.gridview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Button logOut = (Button) findViewById(R.id.buttonLogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLoginState();
                backToLoginScreen();

            }
        });
        if(checkIsTablet()){
            gridView.setNumColumns(4);
        }else{
            gridView.setNumColumns(2);
        }

        dataList = new ArrayList<>();
        adapter = new LazyAdapter(
                MainActivity.this, dataList);
        if (isConnectingToInternet()) {
            String url = BASE_SERVER_URL + PAGE_0;
            dataFromServer(url);
        } else {
            alertTurnOnWifi();
        }
        gridView.setOnScrollListener(new EndlessScrollList() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (licznik < MAX_PAGE_NUMBER) {
                    progressBar.setVisibility(View.VISIBLE);
                    licznik++;
                    nextJsonDataGet(licznik);
                }
                return true;

            }
        });

    }
    private boolean checkIsTablet() {
        boolean isTablet = false;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        if (diagonalInches >= 7.0) {
            isTablet = true;
        }

        return isTablet;
    }

    private void nextJsonDataGet(int licznik) {
        String pageNumber = Integer.toString(licznik);
        String url = BASE_SERVER_URL + PAGE + pageNumber + PAGE_END_FILE;
        dataFromServer(url);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    private void dataFromServer(String url) {
        GetJsonData getJsonData = new GetJsonData(MainActivity.this, url,
                new AsyncInterface() {

                    @Override
                    public void data(String jsonString) {
                        if (jsonString.equals(ERROR_GET_DATA_FROM_SERVER)){
                            Toast.makeText(getApplicationContext(), R.string.error_get_data, Toast.LENGTH_LONG).show();

                        }else {
                            dataList = ParseJSON(jsonString);
                            progressBar.setVisibility(View.GONE);
                            gridView.setAdapter(adapter);
                        }
                    }
                });
        getJsonData.execute();
    }

    private void alertTurnOnWifi() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getText(R.string.connect_to_internet))
                .setTitle(getResources().getText(R.string.connect_to_internet_alert_title))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent settings = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(settings);
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                .show();
    }

    private void backToLoginScreen() {
        Intent intent = new Intent(this, LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void resetLoginState() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PREFS_F, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            if (connectivityManager != null) {
                //deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String jsonResponse) {
        if (jsonResponse != null) {
            try {

                JSONObject jsonObj = new JSONObject(jsonResponse);

                JSONArray dataItems = jsonObj.getJSONArray(TAG_ARRAY);

                for (int i = 0; i < dataItems.length(); i++) {
                    JSONObject c = dataItems.getJSONObject(i);

                    String title = c.getString(TAG_TITLE);
                    String desc = c.getString(TAG_DESC);
                    String imageUrl = c.getString(TAG_URL);


                    HashMap<String, String> item = new HashMap<>();

                    item.put(TAG_TITLE, title);
                    item.put(TAG_DESC, desc);
                    item.put(TAG_URL, imageUrl);

                    dataList.add(item);
                }
                return dataList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }
}


