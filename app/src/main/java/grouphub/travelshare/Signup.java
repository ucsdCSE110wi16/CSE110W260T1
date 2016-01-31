package grouphub.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.LinkedList;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Signup";
    private EditText passwordField;
    private EditText usernameField;
    private EditText emailField;
    private EditText confirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        passwordField = (EditText) findViewById(R.id.passwordSignUp);
        usernameField = (EditText) findViewById(R.id.username);
        emailField = (EditText) findViewById(R.id.emailSignUp);
        confirmPasswordField = (EditText) findViewById(R.id.confpasswordSignUp);

        Button signup = (Button) findViewById(R.id.signupButton);

        signup.setOnClickListener(this);

    }

    private boolean signUp() {
        String password = passwordField.getText().toString();
        String username =usernameField.getText().toString();
        String email = emailField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        boolean errors = false;

        if(password == "" || email == "" || username == "" || confirmPassword == "") {
            errors = true;
        }

        if(!password.equals(confirmPassword)) {
            errors = true;
        }

        ParseUser user = new ParseUser();
       // user.setEmail(email);
        user.setPassword(password);
        user.setUsername(email);

        user.put("screenName", username);
        user.put("groups", new LinkedList<ParseObject>());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(Signup.this, "SIGNED UP", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Signup.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }


    @Override
    public void onClick(View view) {
        signUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
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
