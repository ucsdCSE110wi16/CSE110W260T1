package grouphub.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private Button login;
    private EditText email;
    private EditText password;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize facebook login stuff and callback manager
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        setContentView(R.layout.login);
        Button signUp = (Button) findViewById(R.id.signupButton);
        Button login = (Button) findViewById(R.id.loginButton);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // facebook callback manager for facebook login
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    // Returns true if correct login and false otherwise
    private void login() {
        String password = this.password.getText().toString();
        String email = this.email.getText().toString();

        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    successfulLogin();
                } else if (user == null) {
                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Login.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void successfulLogin() {
        Toast.makeText(Login.this, "HORRAY YOU LOGINED IN", Toast.LENGTH_LONG).show();
        Intent intent_home = new Intent(Login.this, Main.class);
        startActivity(intent_home);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId() ){
            case R.id.loginButton:
                login();
                break;
            case R.id.signupButton:
                Intent intent_signup = new Intent(Login.this, Signup.class);
                startActivity(intent_signup);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}