package grouphub.travelshare;

import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;
import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by Christopher on 3/7/2016.
 */
@RunWith(AndroidJUnit4.class)
public class CameraTest {
    @Rule
    public ActivityTestRule<Main> activityTestRule =
            new ActivityTestRule<>(Main.class);


    @Test
    public void CameraCheck() {

        Intents.init();
        activityTestRule.launchActivity(new Intent());

        try {
            Thread.sleep(2000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.button_camera), FirstViewMatcher.firstView())).perform(click());

        intended(anyIntent());

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        Intents.release();
    }

}
