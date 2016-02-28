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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment implements Serializable{
    private transient View view;
    private transient ListView mainView;
    private transient List<PictureViewModel> viewModels; // to keep track of photos on homepage

    private static final String TAG = "HomepageFragment";

    public HomepageFragment() {
        // Required empty public constructor
        view = null;
    }

    public static HomepageFragment newInstance() {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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

        if(TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser()) == null)
            Log.d(TAG, "Problem accessing current travel group");

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_homepage, container, false);

            mainView = (ListView) view.findViewById(R.id.listview_pictures);

            viewModels = new ArrayList<PictureViewModel>();

            initializePictures();
        }

        return view;
    }

    private void initializePictures() {
        TravelGroup gr;

        try {
            gr = TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser());
            ArrayList<Photo> photos = gr.getPhotos();

            if(photos == null) {
                return;
            } else {
                addPicturesToView(photos);
            }

        } catch (Exception e){
            Log.d(TAG, "No groups listed for the user");
        }

    }

    // for adding many pictures
    private void addPicturesToView(ArrayList<Photo> photos) {
        for (int i = photos.size() - 1; i >= 0; i--) {
            PictureViewModel row = new PictureViewModel(photos.get(i).getCityName() + "\n" + photos.get(i).getDate(),
                                                        photos.get(i).getPhotoUrl());
            viewModels.add(row);
        }

        PictureListViewAdapter adapter = new PictureListViewAdapter(getActivity(), viewModels);
        mainView.setAdapter(adapter);
    }

    // for adding a single picture
    public void addPictureToView(Photo photo) {
        PictureViewModel row = new PictureViewModel(photo.getCityName() + "\n" + photo.getDate(),
                photo.getPhotoUrl());
        viewModels.add(row);

        PictureListViewAdapter adapter = new PictureListViewAdapter(getActivity(), viewModels);
        mainView.setAdapter(adapter);
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
