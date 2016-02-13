package grouphub.travelshare;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by kenme_000 on 2/6/2016.
 */
@ParseClassName("PhotoLibrary")
public class PhotoLibrary extends ParseObject {
    private static final String TAG = "PhotoLibrary";

    /* Default no arg constructor required by Parse subclass
    * DO NOT MODIFY ANY FIELDS IN THIS CONSTRUCTOR AS PARSE
    * DOES NOT ALLOW IT
    */
    public PhotoLibrary() {
        super();
    }

    /*
     * Constructor for creating first every PhotoLibrary
     * The parameter means nothing...its only there to
     * distinguish between the default no arg constructor
     * that is needed by Parse
     */
    public PhotoLibrary(boolean savePhotoLibrary) {
        super();
        ArrayList<Photo> photosParse = new ArrayList<Photo>();
        put("photos", photosParse);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error creating PhotoLibrary: " + e);
                }

            }
        });
    }

    /*
     * For grabbing a pre-existing photo library that was stored in the Parse database
     */
    public static PhotoLibrary getPhotoLibrary(String id) {
        ParseQuery<PhotoLibrary> query = ParseQuery.getQuery(TAG);
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
    public void addPhoto(Photo pic) {
        add("photos", pic);
        saveInBackground(new SaveCallback() {
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
        return (ArrayList<Photo>) get("photos");
    }

    //TODO: CHANGE THIS METHOD TO CALL CLOUD FUNCTION THAT CALCULATES THE MOST RECENT PHOTOS
    // OLD CODE MOVE THIS TO THE CLOUD...DO NOT USE
    private ArrayList<Photo> getMostRecentPhotos(int numPhotos) {
        ArrayList<Photo> mostRecent =  new ArrayList<Photo>();
        /*if(photos != null) {
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
        }*/
        return mostRecent;
    }
}
