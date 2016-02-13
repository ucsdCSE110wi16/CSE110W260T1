package grouphub.travelshare;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by Kenan Mesic and Christopher Chinowth on 1/30/2016.
 */

@ParseClassName("TravelGroup")
public class TravelGroup extends ParseObject {
    private static final String TAG = "TravelGroup";

    // Default no arg constructor required by Parse subclass
    public TravelGroup() {
        super();
    }

    // Constructor to call usually for creating a TravelGroup
    public TravelGroup(ParseUser leader, String groupName) {
        super();
        ArrayList<String> userNames = new ArrayList<String>();
        userNames.add(leader.getUsername());
        addAll("users", userNames);
        put("leader", leader);
        put("groupName", groupName);
        ArrayList <TravelGroup> groups = (ArrayList) leader.get("groups");
        groups.add(0, this);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.d(TAG, "Error in creating TravelGroup: " + e);
                }
            }
        });
        leader.put("groups", groups);
        leader.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error occured in updating leader with travel group");
                }
            }
        });
    }

    //TODO send a request to invite a user
    public void inviteUser(ParseUser user) {
        return;
    }

    // Return unique id of the object
    public String getId() {
        return getObjectId();
    }

    //Add the current user logged in
    public void addUser(ParseUser user) {
        // Create a list with user
        ArrayList<String> newUser = new ArrayList<String>();
        newUser.add(user.getUsername());
        addAll("users", newUser);
        ArrayList<TravelGroup> groups = (ArrayList) user.get("groups");
        groups.add(0, this);
        saveInBackground();
        user.put("groups", groups);
        user.saveInBackground();
    }

    // Get the description of the group
    public String getDescription() {
        return getString("description");
    }

    // Set the desciption of the group
    public void setDescription(String description) {
        put("description", description);
        saveInBackground();
    }

    // Get the leader/admin of the group
    public ParseUser getLeader() {
        return getParseUser("leader");
    }

    // Set the leader/admin of the group
    public void setLeader(ParseUser leader) {
        put("leader", leader);
        saveInBackground();
    }

    // Get all the usernames of the users in the group
    public ArrayList<String> getUsers() {
        return (ArrayList<String>) get("users");
    }

    // Get the group name of the group
    public String getGroupName() {
        return getString("groupName");
    }

    // Set the group name
    public void setGroupName(String groupName) {
        put("groupName", groupName);
        saveInBackground();
    }

    public static TravelGroup getActiveTravelGroup(ParseUser user) {
        //TODO: create method to return active Travel Group of user
        return null;
    }

    public static ArrayList<TravelGroup> getTravelGroups(ParseUser user) {
        //TODO: create method to return list of travel groups connected to user
        return null;
    }
}
