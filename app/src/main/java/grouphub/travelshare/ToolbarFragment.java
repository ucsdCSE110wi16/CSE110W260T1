package grouphub.travelshare;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ToolbarFragment extends Fragment implements View.OnClickListener {
    View view;

    // Declare toolbar buttons
    private Button buttonFolders;
    private Button buttonManager;
    private Button buttonCamera;
    private Button buttonViews;
    
    // All buttons start out as not being pressed
    private Boolean buttonFoldersPressed = false;
    private Boolean buttonManagerPressed = false;
    private Boolean buttonViewsPressed = false;

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

    private Fragment homepageFragment,groupFragment, folderFragment = null;

    public ToolbarFragment() {
        // Required empty public constructor
        homepageFragment = new HomepageFragment();
        folderFragment = new FoldersFragment();
        groupFragment = new GroupFragment();

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

        buttonFolders = (Button) view.findViewById(R.id.button_folders);
        buttonCamera = (Button) view.findViewById(R.id.button_camera);
        buttonManager = (Button) view.findViewById(R.id.button_manager);
        buttonViews = (Button) view.findViewById(R.id.button_views);

        buttonFolders.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
        buttonManager.setOnClickListener(this);
        buttonViews.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        FragmentManager Manager = getFragmentManager();
        FragmentTransaction trans = Manager.beginTransaction();

        // For the functionality of the buttons
        switch (view.getId()) {
            case R.id.button_folders:
                if (buttonFoldersPressed) {
                    trans.replace(R.id.placeholder, homepageFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                    resetButtonPress();
                } else {
                    trans.replace(R.id.placeholder, folderFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                    buttonFoldersPressed = true;
                }
                break;
            case R.id.button_camera:
                useCamera();
                break;
            case R.id.button_manager:
                if (buttonManagerPressed) {
                    trans.replace(R.id.placeholder, homepageFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                    resetButtonPress();
                } else {
                    trans.replace(R.id.placeholder, groupFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                    resetButtonPress();
                    buttonManagerPressed = true;
                }
                break;
            case R.id.button_views:
                break;
        }

    }

    // Resets button presses (and for now button colors)
    public void resetButtonPress() {
        buttonManagerPressed = false;
        buttonFoldersPressed = false;
        buttonViewsPressed = false;
    }

    // I wonder if fragment is actually deleted or put into some background mode... may affect performance
    public void switchPage(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.placeholder, fragment, tag);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void useCamera() {

        //start listening for user location.
        initializeLocationListener();

        //must check for permissions at run time.
        if((int) Build.VERSION.SDK_INT < 23 ||checkLocationPermission())
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

                //must check for permissions at runtime
                if((int) Build.VERSION.SDK_INT < 23 || checkLocationPermission())
                    locationManager.removeUpdates(locationListener);

                if(currentLoc != null) {
                    // print out latitude and longitude to log
                    Log.d("latitude", "latitude: " + currentLoc.getLatitude());
                    Log.d("longitude", "longitude: " + currentLoc.getLongitude());
                } else {
                    // if no location is found print out filler to log
                    Log.d("latitude", "latitude: " + " No Location");
                    Log.d("longitude", "longitude: " + " No Location");
                }

                // Image captured and saved to fileUri specified in the Intent
                Bitmap bitmap = BitmapFactory.decodeFile(uriSavedImage.toString().substring(7)); // get correct fileurl
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // 0 for small size, 100 for quality

                byte[] image = stream.toByteArray();


                List<Address> addresses = null;
                String cityName = "";
                Geocoder geocoder;
                try {
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    addresses = geocoder.getFromLocation(currentLoc.getLatitude(), currentLoc.getLongitude(), 1);
                    cityName = addresses.get(0).getAddressLine(0);
                } catch(Exception e) {}

                // Get the date, append milliseconds after it to have a unique photo name for upload to parse
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                formattedDate += " " + c.getTimeInMillis();

                // CREATE NEW PHOTO HERE. USE THE PHOTO CLASS, IT WILL WRITE TO PARSE DATABASE
                // Photo photo = new Photo(currentLoc, image, ParseUser.getCurrentUser());
                Photo photo = new Photo(cityName,formattedDate,image, ParseUser.getCurrentUser());
                TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser()).addPhoto(photo);
                bitmap.recycle();
                try {
                    stream.close();
                } catch(Exception e) {}

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

        Location currentLoc = null;

        //must check for permission at run time.
        if((int) Build.VERSION.SDK_INT < 23 || checkLocationPermission())
            currentLoc = locationManager.getLastKnownLocation(locationProvider);

        if (currentLoc != null){
            lastKnownLocation = currentLoc;
            return currentLoc;
        } else {
            return lastKnownLocation;
        }
    }

    private boolean checkLocationPermission()
    {

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getContext().checkCallingPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);

        //todo Handle case where permission isn't granted, popup to user or something
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
