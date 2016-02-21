package grouphub.travelshare;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;
//import android.widget.Toolbar;



public class Main extends AppCompatActivity {
    private Spinner dropdown;
    String[] menu_items = {"Settings", "Logout"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize homepage and toolbar fragments

        Fragment fragmentH= new HomepageFragment();
        Fragment fragmentT = new ToolbarFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_toolbar, fragmentT);
        fragmentTransaction.add(R.id.placeholder, fragmentH);
        fragmentTransaction.commit();

        // Spinner implementation
        dropdown = (Spinner) findViewById(R.id.dropdown_menu);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Magic???
                ((TextView)view).setText(null);

                switch (position) {
                    case 0:
                        // Add settings stuff here
                        break;
                    case 1:
                        // User chose the "logout_button" item, do the logout...
                        LoginManager.getInstance().logOut(); // logout facebook
                        ParseUser.logOut(); // logout parse user

                        // then take the user back to login screen
                        Intent login_intent = new Intent(Main.this, Login.class);
                        startActivity(login_intent);
                        finish();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing happens if nothing is selected
            }
        });

        dropdown.setAdapter(new ArrayAdapter<String>(Main.this, R.layout.spinner_layout, menu_items));
    }

}
