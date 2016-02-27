package grouphub.travelshare;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
    // DO NOT MODIFY ANY FIELDS IN THIS CONSTRUCTOR AS PARSE
    // DOES NOT ALLOW IT
    public TravelGroup() {
        super();
    }

    // Constructor to call usually for creating a TravelGroup
    public TravelGroup(ParseUser leader, String groupName) {
        super();
        PhotoLibrary photoLibrary = new PhotoLibrary(true);
        add("users", leader.getUsername());
        put("leader", leader);
        put("groupName", groupName);
        put("photolibrary", photoLibrary);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.d(TAG, "Error in creating TravelGroup: " + e);
                }
            }
        });
        leader.add("groups", this);
        leader.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error occured in updating leader with travel group");
                }
            }
        });
    }

    /*
    * For grabbing a pre-existing Travel Group that was stored in the Parse database
    */
    public static TravelGroup getTravelGroup(String id) {
        ParseQuery<TravelGroup> query = ParseQuery.getQuery(TAG);
        try {
            return query.get(id);
        }
        catch(ParseException e) {
            Log.d(TAG, "Failed grabbing the TravelGroup: " + e);
            return null;
        }
    }

    // send a request to the user
    public void inviteUser(ParseUser user) {
        String invitedUsersGroup = getActiveTravelGroup(user).getId();

        // get the groupID of the current user's active travel group
        String groupID = getActiveTravelGroup(ParseUser.getCurrentUser()).getId();

        if (invitedUsersGroup.equals(groupID)) {
            return;
        }
        // now send the invite to the user
        user.put("invitationID", groupID);
    }

    // Return unique id of the object
    public String getId() {
        return getObjectId();
    }

    //Add the current user logged in
    public void addUser(ParseUser user) {
        add("users", user.getUsername());
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error in adding user to travel group: " + e);
                }
            }
        });
        user.add("groups", this);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error adding travel group to user: " + e);
                }
            }
        });
    }

    // Get the description of the group
    public String getDescription() {
        try {
            return fetchIfNeeded().getString("description");
        }
        catch (ParseException e) {
            Log.d(TAG, "Error in retrieving description of group: " + e);
            return null;
        }
    }

    // Set the desciption of the group
    public void setDescription(String description) {
        put("description", description);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error setting the group description: " + e);
                }
            }
        });
    }

    // Get the leader/admin of the group
    public ParseUser getLeader() {
        try {
            return fetchIfNeeded().getParseUser("leader");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in retrieving leader of group: " + e);
            return null;
        }
    }

    // Add photo which will add it to the photo library
    public void addPhoto(Photo pic) {
        PhotoLibrary lib = getPhotoLibrary();
        if(lib == null) {
            return;
        }
        lib.addPhoto(pic);
    }

    // Get the current photoLibrary
    public PhotoLibrary getPhotoLibrary() {
        try {
            return (PhotoLibrary) fetchIfNeeded().getParseObject("photolibrary");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in retrieving the photo library: " + e);
            return null;
        }
    }

    // Set the leader/admin of the group
    public void setLeader(ParseUser leader) {
        put("leader", leader);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error setting group leader: " + e);
                }
            }
        });
    }

    // Get all the usernames of the users in the group
    public ArrayList<String> getUsers() {
        try {
            return (ArrayList<String>) fetchIfNeeded().get("users");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in retrieving the list of users for the group: " + e);
            return null;
        }
    }

    // Get the group name of the group
    public String getGroupName() {
        try {
            return fetchIfNeeded().getString("groupName");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in retrieving the group name: " + e);
            return null;
        }
    }

    // Set the group name
    public void setGroupName(String groupName) {
        put("groupName", groupName);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error setting group name: " + e);
                }
            }
        });
    }


    public static TravelGroup getActiveTravelGroup(ParseUser user) {

        ArrayList<TravelGroup> groups = (ArrayList<TravelGroup>) user.get("groups");

        if(groups == null || groups.size() == 0) {

            return null;
        }

        TravelGroup gr = groups.get(groups.size() - 1);

        return gr;
        //return groups.get(groups.size() - 1);
    }

    public static ArrayList<TravelGroup> getTravelGroups(ParseUser user) {
        //TODO: create method to return list of travel groups connected to user
        try {
            return (ArrayList<TravelGroup>) user.fetchIfNeeded().get("groups");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in retrieving the travel groups associated with the user: " +  e);
            return null;
        }
    }

    // Get all photos from photo library
    public ArrayList<Photo> getPhotos() {
        return getPhotoLibrary().getPhotos();
    }
}
