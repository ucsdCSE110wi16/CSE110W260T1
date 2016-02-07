package grouphub.travelshare;

import android.app.ProgressDialog;
import android.location.Location;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
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
public class Photo {
    private String objectid;
    private double latitude;
    private double longitude;
    private static final String TAG = "PhotoLibrary";
    private ParseObject photoObject;
    private ParseFile pic;
    private ArrayList<ParseObject> comments;
    private ParseUser creater;

    public Photo(Location geolocation, byte[] data, ParseUser creater, ProgressDialog dialog) {
        photoObject = new ParseObject(TAG);
        comments = new ArrayList<ParseObject>();
        pic = new ParseFile(creater.get("screenName") + ".jpg", data);
        photoObject.put("comments", comments);
        latitude = geolocation.getLatitude();
        longitude = geolocation.getLongitude();
        photoObject.put("latitude", latitude);
        photoObject.put("longitude", longitude);
        photoObject.put("pic", pic);
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

        photoObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    objectid = photoObject.getObjectId();
                } else {
                    Log.d(TAG, "Error creating photo in Parse: " + e);

                }

            }
        });
    }
    public Photo(String id) {
        this.objectid = id;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TAG);
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    photoObject = object;
                    comments = (ArrayList<ParseObject>) photoObject.get("comments");
                    latitude = Double.parseDouble((String) photoObject.get("longitude"));
                    longitude = Double.parseDouble((String) photoObject.get("latitude"));
                    creater = (ParseUser) photoObject.getParseUser("creator");
                    pic = (ParseFile) photoObject.getParseFile("pic");
                } else {
                    Log.d(TAG, "Error in retrieving Photo Library: " + e);
                }
            }
        });
    }

    public Photo(ParseObject photo) {
        this.objectid = photo.getObjectId();
        photoObject = photo;
        comments = (ArrayList<ParseObject>) photoObject.get("comments");
        latitude = Double.parseDouble((String) photoObject.get("longitude"));
        longitude = Double.parseDouble((String) photoObject.get("latitude"));
        creater = (ParseUser) photoObject.getParseUser("creator");
        pic = (ParseFile) photoObject.getParseFile("pic");
    }

    public void updatePhoto() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TAG);
        query.getInBackground(objectid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    photoObject = object;
                    comments = (ArrayList<ParseObject>) photoObject.get("comments");
                    latitude = Double.parseDouble((String) photoObject.get("longitude"));
                    longitude = Double.parseDouble((String) photoObject.get("latitude"));
                    creater = (ParseUser) photoObject.getParseUser("creator");
                    pic = (ParseFile) photoObject.getParseFile("pic");
                } else {
                    Log.d(TAG, "Error in retrieving Photo Library: " + e);
                }
            }
        });
    }

    public void addComment(String content, String screenName) {
        ParseObject comment = new ParseObject("comment");
        comment.put("content", content);
        comment.put("screenName", screenName);
        ArrayList<ParseObject> comments = (ArrayList) photoObject.get("comments");
        comments.add(0, comment);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error creating comment in Parse: " + e);
                }

            }
        });
        photoObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error saving comment to photo in Parse: " + e);
                }

            }
        });
    }

    public String getId() {
        return objectid;
    }

    public byte[] getData() {
        try {
            return pic.getData();

        }
        catch (ParseException e) {
            Log.d(TAG, "Error in getting photo from Parse: " + e);
            return null;
        }
    }

    public ParseObject getPhotoObject() {
        return photoObject;
    }

}
