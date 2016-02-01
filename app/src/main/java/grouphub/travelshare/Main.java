package grouphub.travelshare;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;

public class Main extends AppCompatActivity {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomepageFragment fragment_homepage = new HomepageFragment();
        fragmentTransaction.add(R.id.placeholder, fragment_homepage);
//        getSupportFragmentManager().beginTransaction().add(R.id.placeholder, fragment_homepage);
        fragmentTransaction.commit();
    }

}
