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
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule<Login> activityTestRule =
            new ActivityTestRule<>(Login.class);

    @Test
    public void SignupTestCheck() {

        onView(withId(R.id.email)).perform(typeText("EspressoEmail")).check(matches(withText("EspressoEmail")));
        onView(withId(R.id.password)).perform(typeText("EspressoPass")).check(matches(withText("EspressoPass")));

        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(8000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        //onView(withId(R.id.homepage_main_view)).check(matches(withId(R.id.homepage_main_view)));

    }

}
