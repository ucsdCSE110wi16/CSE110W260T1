package grouphub.travelshare;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;
//import android.widget.Toolbar;



public class Main extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragmentT = getFragmentManager().findFragmentById(R.id.fragment_toolbar);
        Fragment fragmentH = getFragmentManager().findFragmentById(R.id.placeholder);
        setContentView(R.layout.activity_main);

        // Initialize homepage and toolbar fragments

        fragmentH= new HomepageFragment();
        fragmentT = new ToolbarFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_toolbar, fragmentT);
        fragmentTransaction.add(R.id.placeholder, fragmentH);
        fragmentTransaction.commit();

        Spinner spinner = (Spinner) findViewById(R.id.dropdown_menu);
        spinner.setOnItemSelectedListener(this);
    }

    // Spinner implementations
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        // Magic???
        ((TextView)view).setText(null);

        switch(pos) {
            case 0:
                // There is a problem with this not showing up right away after pressing...
                // must first exit out of app and return
/*                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();*/
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

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
