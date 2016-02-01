package grouphub.travelshare;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;


/**
 * Created by Kenan Mesic on 1/22/2016.
 *
 * Application class..used when app opens up orginally
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Create a local datastore for Parse objects
        Parse.enableLocalDatastore(this);

        // Initialize Parse so that it can be used in the application
        Parse.initialize(this);

        ParseFacebookUtils.initialize(this);
    }
}
