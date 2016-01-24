package pl.hekko.kesser.dominikkesselring;/*
 * Created by kesser on 23.01.16.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})";
    private static final String ERROR_MAIL_FORMAT = "Błędny format Mail!";
    private static final String ERROR_PASS_FORMAT = "Błędny format Hasło!";
    private static final int PREFERENCE_MODE_PRIVATE = 1;
    private EditText loginMail;
    private EditText loginPassword;
    private Pattern patternMail;
    private Pattern patternPass;
    private Matcher matcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        patternMail = Pattern.compile(EMAIL_PATTERN);
        patternPass = Pattern.compile(PASSWORD_PATTERN);
        loginMail = (EditText) findViewById(R.id.loginMail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        Button loginInButton = (Button) findViewById(R.id.buttonLogIn);
        loginInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail = loginMail.getText().toString();
                String userPassword = loginPassword.getText().toString();
                if (validateEmail(userMail)) {
                    loginMail.setError(ERROR_MAIL_FORMAT);
                } else if (validatePassword(userPassword)) {
                    loginPassword.setError(ERROR_PASS_FORMAT);
                } else {
                    doLogin();
                }

            }
        });
    }

    private void doLogin() {
            saveLoginState();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
    }

    private void saveLoginState() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", PREFERENCE_MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", true);
        editor.apply();
    }

    private boolean validateEmail(String user){
        matcher = patternMail.matcher(user);
        return !matcher.matches();
    }

    private boolean validatePassword(String password) {
        matcher = patternPass.matcher(password);
        return !matcher.matches();
    }

    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();

    }
}