package grouphub.travelshare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;

/**
 * Created by Christopher on 3/8/2016.
 */
@RunWith(AndroidJUnit4.class)
public class InviteMessageTest {

    @Rule
    public ActivityTestRule<Main> activityTestRule =
            new ActivityTestRule<>(Main.class);

    @Test
    public void InviteMessageCheck() {

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.dropdown_menu)).perform(click());

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onData(anything()).atPosition(1).perform(click());

        try {
            Thread.sleep(3000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login_button)).check(matches(withId(R.id.login_button)));

        onView(withId(R.id.email)).perform(typeText("EspressoTestSignup")).check(matches(withText("EspressoTestSignup")));
        onView(withId(R.id.password)).perform(typeText("espresso")).check(matches(withText("espresso")));

        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(3000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }



        onView(withId(R.id.listview_pictures)).check(matches(withId(R.id.listview_pictures)));

        onView(withText("You have received an invitation to join")).inRoot(isDialog()).check(matches(isDisplayed()));


    }

}
