package grouphub.travelshare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.widget.Toolbar;



public class Main extends AppCompatActivity {
    FragmentManager fragmentManager = getFragmentManager();

    ToolbarFragment fragment_toolbar = new ToolbarFragment();
    HomepageFragment fragment_homepage = new HomepageFragment();
    GroupFragment fragment_group = new GroupFragment();
    FoldersFragment fragment_folders = new FoldersFragment();

    private Uri uriSavedImage;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // A new fragment manager must be created for each fragment operation
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        setContentView(R.layout.activity_main);

        fragmentTransaction.add(R.id.fragment_toolbar, fragment_toolbar);
        fragmentTransaction.add(R.id.placeholder, fragment_homepage);
        fragmentTransaction.commit();
    }

    public void switchToHomepage() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.placeholder, fragment_homepage);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void switchToGroupManager() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.placeholder, fragment_group);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void switchToFolders() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.placeholder, fragment_folders);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

}
