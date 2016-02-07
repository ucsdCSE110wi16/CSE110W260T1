package grouphub.travelshare;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by kenme_000 on 2/6/2016.
 */
public class PhotoLibrary {
    private String objectid;
    private static final String TAG = "PhotoLibrary";
    private ParseObject photoLibrary;
    private ArrayList<ParseObject> photosParse;
    private ArrayList<Photo> photos;

    /*
     * Constructor for creating first every PhotoLibrary
     */
    public PhotoLibrary() {
        photoLibrary = new ParseObject(TAG);
        photosParse = new ArrayList<ParseObject>();
        photoLibrary.put("photos", photosParse);
        photoLibrary.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    objectid = photoLibrary.getObjectId();
                } else {
                    Log.d(TAG, "Error creating PhotoLibrary: " + e);

                }

            }
        });
        photos = new ArrayList<Photo>();
    }

    /*
     * Constructor for intializing a preexisting photo library that was store in the Parse database
     */
    public PhotoLibrary(String id) {
        this.objectid = id;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TAG);
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    photoLibrary = object;
                    getPhotos();
                } else {
                    Log.d(TAG, "Error in retrieving Photo Library: " + e);
                }
            }
        });
    }

    /*
     * Add photo to the Photo Library in parse
     */
    public void addPhoto(Photo pic) {
        photos.add(photos.size(), pic);
        ParseObject photo = pic.getPhotoObject();
        photosParse.add(0, photo);
        photoLibrary.add("photos", photosParse);
        photoLibrary.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error adding photo to library: " + e);
                }
            }
        });
    }

    //TODO: getPhotos is fine except for trying to receive thousands of photos
    //TODO: need to add a max limit to num of photos getting received because user cannot view them
    //TODO: all
    public ArrayList<Photo> getPhotos() {
        if(photoLibrary == null) {
            Log.d(TAG, "Error: Did not initialize Photo Library correctly");
            throw new NullPointerException();
        }
        // if photos were already received
        else if(photos != null) {
            return photos;
        }
        else {
            photosParse = (ArrayList) photoLibrary.get("photos");
            photos = new ArrayList<Photo>();
            for(int i=0; i < photosParse.size(); i++) {
                photos.add(i, new Photo(photosParse.get(i)));
            }
            return photos;
        }
    }

    //TODO: CHANGE THIS METHOD TO CALL CLOUD FUNCTION THAT CALCULATES THE MOST RECENT PHOTOS
    public ArrayList<Photo> getMostRecentPhotos(int numPhotos) {
        ArrayList<Photo> mostRecent =  new ArrayList<Photo>();
        if(photos != null) {
            for(int i=0; i < photos.size() && i < numPhotos; i++) {
                mostRecent.add(i, photos.get(i));
            }
        }
        else {
            photosParse = (ArrayList<ParseObject>) photoLibrary.get("photos");
            photos = new ArrayList<Photo>();
            for(int i=0; i < photosParse.size(); i++) {
                if(i < numPhotos) {
                    mostRecent.add(i, new Photo(photosParse.get(i)));
                    photos.add(i, new Photo(photosParse.get(i)));
                }
                // Once all of most recent is found then return that but spawn a new thread
                // in the background to get all the photos for later use
                else {
                    final int photosLeft = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized(this) {
                                for (int i = photosLeft; i < photosParse.size(); i++) {
                                    photos.add(i, new Photo(photosParse.get(i)));
                                }
                            }
                        }
                    }).start();
                }
            }
        }
        return mostRecent;
    }
}
