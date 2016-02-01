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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private Button login;
    private EditText email;
    private EditText password;

    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> Callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //Successful Facebook Login
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
            }

            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                String email = (String) object.get("email");
                                String name = (String) object.get("name");
                                ParseUser user = new ParseUser();
                                // user.setEmail(email);
                                user.setPassword("");
                                user.setUsername(email);

                                user.put("screenName", name);

                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {

                                        } else {
                                            successfulLogin();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {
            // User cancels facebook login request
        }

        @Override
        public void onError(FacebookException exception) {
            //Facebook login fails(for example internet fails mid login.
            Toast.makeText(Login.this, "Could not connect to Facebook", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if user is already logged in
        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            return;
        }

        // initialize facebook login stuff and callback manager
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_friends", "email", "user_birthday"));
        loginButton.registerCallback(callbackManager, Callback);

        Button signUp = (Button) findViewById(R.id.signupButton);
        Button login = (Button) findViewById(R.id.loginButton);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
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
                } else {
                    Toast.makeText(Login.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void successfulLogin() {
        Toast.makeText(Login.this, "HORRAY YOU LOGINED IN", Toast.LENGTH_LONG).show();
       /* ParseQuery<ParseUser> userList = ParseUser.getQuery();
        userList.whereEqualTo("username", "test@gmail.com");
        userList.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size() == 1) {
                    ParseUser test = objects.get(0);
                    TravelGroup group = new TravelGroup(ParseUser.getCurrentUser(), "TEST");
                    group.addUser(test);
                    Toast.makeText(Login.this, "SUCCESS", Toast.LENGTH_LONG).show();
                }
            }
        });*/


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}