package grouphub.travelshare;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.List;

public class GroupFragment extends Fragment implements Serializable{
    private transient View view;

    private transient Button button_creategroup;
    private transient Button button_inviteusertogroup;
    private transient EditText email_textfield;

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
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_group, container, false);

        email_textfield = (EditText) view.findViewById(R.id.usertoinvite);

        button_creategroup = (Button) view.findViewById(R.id.button_creategroup);
        button_creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelGroup travelGroup = new TravelGroup(ParseUser.getCurrentUser(), "Dummy Group");
                Toast.makeText(getActivity(), "Group Created", Toast.LENGTH_LONG).show();
            }
        });

        button_inviteusertogroup = (Button) view.findViewById(R.id.button_inviteusertogroup);
        button_inviteusertogroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the email from text field and pass in to the function below VVVVV
                String email = email_textfield.getText().toString();
                inviteUserToGroup(email);
            }
        });

        // Inflate the layout for this fragment
        return view;

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
                    Log.d(TAG, "ParseQueryError: "+e);
                }
            }

        });

    }

}
