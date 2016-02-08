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

import java.io.ByteArrayOutputStream;
import java.io.File;
//import android.widget.Toolbar;



public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // A new fragment manager must be created for each fragment operation
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        setContentView(R.layout.activity_main);

        fragmentTransaction.add(R.id.fragment_toolbar, new ToolbarFragment());
        fragmentTransaction.add(R.id.placeholder, new HomepageFragment());

        fragmentTransaction.commit();
    }
}
