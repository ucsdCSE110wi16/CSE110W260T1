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
 * Created by Christopher on 3/8/2016.
 * requires FalseInformation to not be a valid account name
 * Or for the password to be incorrect
 */
@RunWith(AndroidJUnit4.class)
public class FailedLoginTest {

    @Rule
    public ActivityTestRule<Login> activityTestRule =
            new ActivityTestRule<>(Login.class);

    @Test
    public void LoginCheck() {

        onView(withId(R.id.email)).perform(typeText("FalseInformation")).check(matches(withText("FalseInformation")));
        onView(withId(R.id.password)).perform(typeText("anything")).check(matches(withText("anything")));

        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(2000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.login_button)).check(matches(withId(R.id.login_button)));

    }

}
