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
        return getString("description");
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
        return getParseUser("leader");
    }

    // Add photo which will add it to the photo library
    public void addPhoto(Photo pic) {
        PhotoLibrary lib = getPhotoLibrary();
        lib.addPhoto(pic);
    }

    // Get the current photoLibrary
    public PhotoLibrary getPhotoLibrary() {
        return (PhotoLibrary) getParseObject("photolibrary");
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
        });    }

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
        //TODO: create method to return active Travel Group of user
        ArrayList<TravelGroup> groups = (ArrayList<TravelGroup>) user.get("groups");

        if(groups.size() == 0) {
            return null;
        }

        return groups.get(groups.size() - 1);
    }

    public static ArrayList<TravelGroup> getTravelGroups(ParseUser user) {
        //TODO: create method to return list of travel groups connected to user
        return (ArrayList<TravelGroup>) user.get("groups");
    }

    // Get all photos from photo library
    public ArrayList<Photo> getPhotos() {
        return getPhotoLibrary().getPhotos();
    }
}
