package grouphub.travelshare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;
//import android.widget.Toolbar;



public class Main extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // A new fragment manager must be created for each fragment operation
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        setContentView(R.layout.activity_main);

        // Initialize homepage and toolbar fragments
        fragmentTransaction.add(R.id.fragment_toolbar, new ToolbarFragment());
        fragmentTransaction.add(R.id.placeholder, new HomepageFragment());

        fragmentTransaction.commit();

        Spinner spinner = (Spinner) findViewById(R.id.dropdown_menu);
        spinner.setOnItemSelectedListener(this);

    }

    // Spinner implementations
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        switch(pos) {
            case 0:
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();
                break;
            case 1:
                // User chose the "logout_button" item, do the logout...
                LoginManager.getInstance().logOut(); // logout facebook
                ParseUser.logOutInBackground(); // logout parse user

                // then take the user back to login screen
                Intent login_intent = new Intent(Main.this, Login.class);
                startActivity(login_intent);
                finish();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
