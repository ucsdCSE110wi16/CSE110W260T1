package grouphub.travelshare;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;

public class HomepageFragment extends Fragment {
    View view;
    //LinearLayout mainView;
    ListView mainView;

    private static final String TAG = "HomepageFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomepageFragment() {
        // Required empty public constructor
        view = null;
    }

    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
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

        // check if the current user has any group invitations, if it does, then receive the invitation
        ParseUser currentUser = ParseUser.getCurrentUser();
        String invitation = (String) currentUser.get("invitationID");
        if (!invitation.equals("0") && !invitation.equals("")){
            receiveInvitation(invitation);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null == view) {
            view = inflater.inflate(R.layout.fragment_homepage, container, false);

            //mainView = (LinearLayout) view.findViewById(R.id.homepage_main_view);
            mainView = (ListView) view.findViewById(R.id.listview_pictures);

            initializePictures();
        }

        return view;
    }

    // Currently just using sample pictures, but later needs to access database and use retrieved data to initialize
    private void initializePictures() {
        // Get photos from parse
        // CREATE NEW PHOTO HERE. USE THE PHOTO CLASS, IT WILL WRITE TO PARSE DATABASE
        // Photo photo = new Photo(currentLoc, image, ParseUser.getCurrentUser());
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.travel4);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        Photo photo = new Photo("SD", "fa",image, ParseUser.getCurrentUser());
        TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser()).addPhoto(photo);*/
        TravelGroup gr;

        try {
            gr = TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser());
            ArrayList<Photo> photos = gr.getPhotos();
            if(photos == null) {
                return;
            }
            for (int i = photos.size() - 1; i >= 0; i--) {
                addPictureToView(photos.get(i));
            }
        } catch (Exception e){
            Log.d(TAG, "No groups listed for the user");
        }

//        addPictureToView("Trevor: Hello Mr. Turtle! \nVladmir: Whoa, Dude. Mr. Turtle is my father", R.drawable.travel1, "Maldives Underwater", "June 17, 2014");
//        addPictureToView("Trevor: Swimming with the big fish", R.drawable.travel2, "Maldives Underwater", "June 17, 2014");
//        addPictureToView("", R.drawable.travel3, "Maldives Underwater", "June 17, 2014");
//        addPictureToView("Kramnik: Duuuuuude \nMelissa: First you were all like whoa, and we were like whoa, and you were like whoa...", R.drawable.travel4, "Maldives Underwater", "June 17, 2014");
//        addPictureToView("", R.drawable.travel5, "Maldives", "June 17, 2014");
//        addPictureToView("", R.drawable.travel6, "Maldives", "June 17, 2014");
//        addPictureToView("Trevor: Well... it's time to eat?\nKatie: Yeah, it's time for them to eat us", R.drawable.travel7, "Maldives", "June 17, 2014");
    }

//    private void addPictureToView(String comment, int pictureId, String where, String when) {
//        PictureFragment newPic = PictureFragment.newInstance(comment, pictureId, where, when);
//        getFragmentManager().beginTransaction().add(R.id.homepage_main_view, newPic, "Hello").commit();
//    }

    private void addPictureToView(Photo photo) {
        PictureFragment newPic = PictureFragment.newInstance("", photo.getPicData(), photo.getCityName(), photo.getDate());
        //getFragmentManager().beginTransaction().add(R.id.homepage_main_view, newPic, "Hello").commit();

    }


    protected void receiveInvitation(String groupID){

        // get the group object based off the groupID
        final TravelGroup group = TravelGroup.getTravelGroup(groupID);
        String groupName = (String) group.get("groupName");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked, accept the invitation!
                        acceptInvitation(group);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked, reject the invitation
                        rejectInvitation();
                        break;
                }
            }
        };

        // pop up a dialog box asking the user if they want to accept/reject the invitation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You have received an invitation to join " + groupName + "! Accept?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    protected void acceptInvitation(TravelGroup group){
        // add the user to the TravelGroup
        group.addUser(ParseUser.getCurrentUser());

        // set the invitations the user has to none
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("invitationID", "0");
        //TODO: UPDATE HOMEPAGE GROUPNAME TITLE AND PICTURES IF USER ACCEPTED INVITE TO A NEW GROUP
    }

    protected void rejectInvitation(){
        // set the invitations the user has to none
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("invitationID", "0");
    }

}
