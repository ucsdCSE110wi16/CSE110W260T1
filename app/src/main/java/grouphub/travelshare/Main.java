package grouphub.travelshare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
