package grouphub.travelshare;

import android.app.ProgressDialog;
import android.location.Location;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by kenme_000 on 2/7/2016.
 */
@ParseClassName("Photo")
public class Photo extends ParseObject {
    private String objectid;
    private double latitude;
    private double longitude;
    private static final String TAG = "PhotoLibrary";
    private ParseObject photoObject;
    private ParseFile pic;
    private ArrayList<ParseObject> comments;
    private ParseUser creater;

    /*
     * Default no arg constructor required by Parse subclass
     * DO NOT MODIFY ANY FIELDS IN THIS CONSTRUCTOR AS PARSE
     * DOES NOT ALLOW IT
     */
    public Photo() {
        super();
    }

    /*
     * Constructor to use when constructing a Photo object
     */
    public Photo(Location geolocation, byte[] data, ParseUser creater, ProgressDialog dialog) {
        super();
        ArrayList<ParseObject> comments = new ArrayList<ParseObject>();
        ParseFile pic = new ParseFile(creater.get("screenName") + ".jpg", data);
        put("comments", comments);
        double latitude = geolocation.getLatitude();
        double longitude = geolocation.getLongitude();
        put("latitude", latitude);
        put("longitude", longitude);
        put("pic", pic);
        //SAVE PIC THEN ONCE DONE, PERFORM SAVE ON THE PHOTO OBJECT
        pic.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.d(TAG, "Error in uploading photo to Parse: " + e);
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {

            }
        });

        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error creating photo in Parse: " + e);
                }

            }
        });
    }

    /*
     * Grab a photo object that already exists in the database
     */
    public static Photo getPhoto(String id) {
        ParseQuery<Photo> query = ParseQuery.getQuery(TAG);
        try {
            return query.get(id);
        }
        catch(ParseException e) {
            Log.d(TAG, "Failed grabbing the Photo: " + e);
            return null;
        }
    }

    /*
     * Set the picture for the photo object
     */
    public void setPic(byte[] data) {
        ParseFile pic = new ParseFile(creater.get("screenName") + ".jpg", data);
        //SAVE PIC THEN ONCE DONE, PERFORM SAVE ON THE PHOTO OBJECT
        pic.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error in uploading photo to Parse: " + e);
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {

            }
        });
        put("pic", pic);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error saving photo to Parse: " + e);
                }
            }
        });
    }

    /*
     * Set the location of the Photo object
     */
    public void setLocation(Location geolocation) {
        double latitude = geolocation.getLatitude();
        double longitude = geolocation.getLongitude();
        put("latitude", latitude);
        put("longitude", longitude);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error saving longitude of photo to Parse: " + e);
                }
            }
        });
    }

    /*
     * Get the location of the Photo object
     */
    public Location getLocation() {
        double latitude = Double.parseDouble(getString("latitude"));
        double longitude = Double.parseDouble(getString("longitude"));
        Location location = new Location(TAG);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    /*
     * Add a comment to the photo object
     */
    public void addComment(String content, String screenName) {
        ParseObject comment = new ParseObject("comment");
        comment.put("content", content);
        comment.put("screenName", screenName);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error creating comment in Parse: " + e);
                }

            }
        });

        add("comments", comment);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error saving comment to photo in Parse: " + e);
                }
            }
        });
    }

    /*
     * Get the unique id of the photo object
     */
    public String getId() {
        return getObjectId();
    }

    /*
     * Get the byte data of the picture from the photo object
     */
    public byte[] getPicData() {
        try {
            ParseFile pic = getParseFile("pic");
            return pic.getData();
        }
        catch (ParseException e) {
            Log.d(TAG, "Error in getting photo from Parse: " + e);
            return null;
        }
    }
}
