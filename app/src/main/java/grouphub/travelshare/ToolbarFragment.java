package grouphub.travelshare;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ToolbarFragment extends Fragment implements View.OnClickListener {
    View view;

    private Button button_folders;
    private Button button_manager;
    private Button button_camera;
    private Button button_views;

    //manage user location information
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String locationProvider;
    private Location lastKnownLocation;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // some variables needed for the camera interface
    private Uri uriSavedImage;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public ToolbarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToolbarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToolbarFragment newInstance(String param1, String param2) {
        ToolbarFragment fragment = new ToolbarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        lastKnownLocation = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_toolbar, container, false);

        button_folders = (Button) view.findViewById(R.id.button_folders);
        button_camera = (Button) view.findViewById(R.id.button_camera);
        button_manager = (Button) view.findViewById(R.id.button_manager);
        button_views = (Button) view.findViewById(R.id.button_views);

        button_folders.setOnClickListener(this);
        button_camera.setOnClickListener(this);
        button_manager.setOnClickListener(this);
        button_views.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;

        switch (view.getId()) {
            case R.id.button_folders:
                fragment = new FoldersFragment();
                switchPage(fragment, "ToFolders");
                break;
            case R.id.button_camera:
                useCamera();
                break;
            case R.id.button_manager:
                fragment = new GroupFragment();
                switchPage(fragment, "ToManager");
                break;
            case R.id.button_views:
                break;
        }
    }

    public void switchPage(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.placeholder, fragment, tag);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void useCamera() {
        //start listening for user location.
        initializeLocationListener();
        locationManager.requestLocationUpdates(locationProvider, 5000, 0, locationListener);
        // do camera stuff
        // this code will test if the camera interface code will work
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "TravelShareTemp");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, "tempImage.jpg");
        uriSavedImage = Uri.fromFile(image);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    // used to process the saved image from the camera
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == -1) {
                //get location
                Location currentLoc = getCurrentLocation();
                //stop listening for location updates
                locationManager.removeUpdates(locationListener);

                // print out latitude and longitude to log
                Log.d("latitude", "latitude: " + currentLoc.getLatitude());
                Log.d("longitude", "longitude: " + currentLoc.getLongitude());

                // Image captured and saved to fileUri specified in the Intent
                Bitmap bitmap = BitmapFactory.decodeFile(uriSavedImage.toString().substring(7)); // get correct fileurl
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
                byte[] image = stream.toByteArray();

                // CREATE NEW PHOTO HERE. USE THE PHOTO CLASS, IT WILL WRITE TO PARSE DATABASE
                // Photo photo = new Photo(currentLoc, image, ParseUser.getCurrentUser());

            } else if (resultCode == 0) {

            } else {
                // Image capture failed, advise user
            }
        }
    }
    /*Create our location listener we can call for retrieving current user location*/
    public void initializeLocationListener(){
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        //Alternativly GPS_PROVIDER
        locationProvider = LocationManager.GPS_PROVIDER;

    }
    /*PRECONDITION:
    locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener) is called
    Returns last known location fromt the location updates system only overwrites last known
    location if it can get location*/
    private Location getCurrentLocation(){

        Location currentLoc = locationManager.getLastKnownLocation(locationProvider);

        if (currentLoc != null){
            lastKnownLocation = currentLoc;
            return currentLoc;
        } else {
            return lastKnownLocation;
        }
    }
}
