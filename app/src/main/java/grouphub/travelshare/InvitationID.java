package grouphub.travelshare;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by Kenan Mesic on 2/6/2016.
 */
@ParseClassName("InvitationID")
public class InvitationID extends ParseObject {
    private static final String TAG = "InvitationID";

    /* Default no arg constructor required by Parse subclass
    * DO NOT MODIFY ANY FIELDS IN THIS CONSTRUCTOR AS PARSE
    * DOES NOT ALLOW IT
    */
    public InvitationID() {
        super();
    }

    /*
     * Constructor for creating first every InvitationID
     * The parameter means nothing...its only there to
     * distinguish between the default no arg constructor
     * that is needed by Parse
     */
    public InvitationID(boolean holder) {
        super();
        put("inviteId", "0");
        try {
            save();
        }
        catch(ParseException e) {
            Log.d(TAG, "Error saving InvitationID: " + e);
        }
    }

    /*
     * For grabbing a pre-existing photo library that was stored in the Parse database
     */
    public static InvitationID getInvitationId(String id) {
        ParseQuery<InvitationID> query = ParseQuery.getQuery(TAG);
        try {
            return query.get(id);
        }
        catch(ParseException e) {
            Log.d(TAG, "Failed grabbing the PhotoLibrary: " + e);
            return null;
        }
    }

    // Get unique object id for the photo library
    public String getId() {
        return getObjectId();
    }
    /*
     * Add photo to the Photo Library in parse
     */
    public void putId(String id) {
        put("inviteId", id);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error adding id to Invitation id: " + e);
                }
            }
        });
    }

    public String getInviteId() {
        try {
            return fetchIfNeeded().getString("inviteId");
        }
        catch (ParseException e) {
            Log.d(TAG, "Error in retrieving invitee id: " + e);
            return null;
        }
    }
}
