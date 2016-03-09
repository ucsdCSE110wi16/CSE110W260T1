package grouphub.travelshare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Christopher on 2/7/2016.
 * requires EspressoEmail to be a valid user account
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule<Login> activityTestRule =
            new ActivityTestRule<>(Login.class);

    @Test
    public void LoginCheck() {

        onView(withId(R.id.email)).perform(typeText("TestingInvitee")).check(matches(withText("TestingInvitee")));
        onView(withId(R.id.password)).perform(typeText("test")).check(matches(withText("test")));

        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(4000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.listview_pictures)).check(matches(withId(R.id.listview_pictures)));

    }

}
