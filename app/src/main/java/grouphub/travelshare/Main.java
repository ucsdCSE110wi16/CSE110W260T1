package grouphub.travelshare;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;
//import android.widget.Toolbar;



public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // A new fragment manager must be created for each fragment operation
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        setContentView(R.layout.activity_main);

        // establish the app bar (the toolbar on top)
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(myToolbar);

        fragmentTransaction.add(R.id.fragment_toolbar, new ToolbarFragment());
        fragmentTransaction.add(R.id.placeholder, new HomepageFragment());

        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_button:
                // User chose the "logout_button" item, do the logout...
                LoginManager.getInstance().logOut(); // logout facebook
                ParseUser.logOutInBackground(); // logout parse user

                // then take the user back to login screen
                Intent login_intent = new Intent(Main.this, Login.class);
                startActivity(login_intent);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_log_in, menu);
        return true;
    }
}
