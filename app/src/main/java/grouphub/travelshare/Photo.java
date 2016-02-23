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
import java.util.Calendar;

/**
 * Created by kenme_000 on 2/7/2016.
 */
@ParseClassName("Photo")
public class Photo extends ParseObject {
    private static final String TAG = "PhotoLibrary";

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
    public Photo(String cityName, String date, byte[] data, ParseUser creater) {
        super();

        Calendar c = Calendar.getInstance();
        String unique = c.get(Calendar.DATE) + c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.MILLISECOND) + "";

        ArrayList<ParseObject> comments = new ArrayList<ParseObject>();
        ParseFile pic = new ParseFile(creater.get("screenName") + unique + ".jpg", data);
        put("comments", comments);
        put("cityName", cityName);
        put("date", date);
        put("pic", pic);
        put("creator", creater);
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
        ParseFile pic = new ParseFile(getObjectId().toString()+ ".jpg", data);
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
     * Get the user that created the photo
     */
    public ParseUser getCreator() {
        try {
            return fetchIfNeeded().getParseUser("creator");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in retrieving the creator: " + e);
            return null;
        }
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
        try {
            double latitude = Double.parseDouble(fetchIfNeeded().getString("latitude"));
            double longitude = Double.parseDouble(fetchIfNeeded().getString("longitude"));
            Location location = new Location(TAG);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            return location;
        }
        catch(ParseException e) {
            return null;
        }
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
            ParseFile pic = fetchIfNeeded().getParseFile("pic");
            return pic.getData();
        }
        catch (ParseException e) {
            Log.d(TAG, "Error in getting photo from Parse: " + e);
            return null;
        }
    }

    public String getCityName() {
        try {
            return fetchIfNeeded().getString("cityName");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in getting city name: ");
            return null;
        }
    }

    public String getDate() {
        try {
            return fetchIfNeeded().getString("date");
        }
        catch(ParseException e) {
            Log.d(TAG, "Error in getting date: " + e);
            return null;
        }
    }
}
