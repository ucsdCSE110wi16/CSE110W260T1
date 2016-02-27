package grouphub.travelshare;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

        //request permissions
        checkCameraPermissions();
        checkStoragePermissions();
        checkLocationPermissions();

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

    protected void checkCameraPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);

        }

    }

    protected void checkLocationPermissions(){


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
        }

    }

    protected void checkStoragePermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
        }
    }

}
