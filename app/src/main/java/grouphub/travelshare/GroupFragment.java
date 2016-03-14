package grouphub.travelshare;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.List;

public class GroupFragment extends Fragment implements Serializable {
    private transient View view;

    private transient HomepageFragment fragmentHomepage; // need the reference for page reset upon group creation
    private transient Button button_creategroup;
    private transient Button button_inviteusertogroup;
    private popupModes popupMode;

    private enum popupModes {
        NEW_GROUP, INVITE
    }

    private final int REQCODE = 0;
    private final String EDIT_TEXT_BUNDLE_KEY = "new data";

    private static final String HOMEPAGE_KEY = "homepage_key";

    public GroupFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)
            fragmentHomepage = (HomepageFragment) getArguments().getSerializable(
                    HOMEPAGE_KEY);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_group, container, false);


        button_creategroup = (Button) view.findViewById(R.id.button_creategroup);
        button_creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // displays a popup window to create group
                FragmentManager fm = getFragmentManager();
                PopupDialog editNameDialog = new PopupDialog("Create New Group", R.layout.popup_groupprompt);
                editNameDialog.setTargetFragment(GroupFragment.this, REQCODE);
                editNameDialog.show(fm, "popup_groupprompt");
                popupMode = popupModes.NEW_GROUP;
            }
        });

        button_inviteusertogroup = (Button) view.findViewById(R.id.button_inviteusertogroup);
        button_inviteusertogroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                PopupDialog editNameDialog = new PopupDialog("Invite New Member", R.layout.popup_invite);
                editNameDialog.setTargetFragment(GroupFragment.this, REQCODE);
                editNameDialog.show(fm, "popup_invite");
                popupMode = popupModes.INVITE;
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    public void setHomepageFragment(HomepageFragment homepageFragment) {
        this.fragmentHomepage = homepageFragment;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        if (requestCode == PopupDialog.getRequestCode()) {
            String editTextString = data.getStringExtra(
                    PopupDialog.getKey());

            switch(popupMode){
                case NEW_GROUP:
                    // upon closing popup window, create group
                    TravelGroup travelGroup = new TravelGroup(ParseUser.getCurrentUser(), editTextString);
                    Toast.makeText(getActivity(), "Group Created:\n" + editTextString, Toast.LENGTH_LONG).show();

                    // reset page upon entering a new group
                    fragmentHomepage.resetGroupName();
                    fragmentHomepage.reinitializePictures();
                    break;
                case INVITE:
                    inviteUserToGroup(editTextString);
                    Toast.makeText(getActivity(), editTextString + "\nwas invited", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    // this will call the inviteUser method in TravelGroup
    private void inviteUserToGroup(String email) {
        if (TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser()) == null) {
            Toast.makeText(getActivity(), "Error: You must create a group first!", Toast.LENGTH_LONG).show();
            return;
        }
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", email);
        query.findInBackground(new FindCallback<ParseUser>() {


            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() != 0) {
                        // The query was successful. Set the userToInvite
                        ParseUser userToInvite = objects.get(0);
                        TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser()).inviteUser(userToInvite);
                        Toast.makeText(getActivity(), "User invited!", Toast.LENGTH_LONG).show();
                    } else {
                        // The user was not found.
                        Toast.makeText(getActivity(), "Error: User not found!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.d("inviteUserToGroup", "ParseQueryError: " + e);
                }
            }

        });

    }

}
