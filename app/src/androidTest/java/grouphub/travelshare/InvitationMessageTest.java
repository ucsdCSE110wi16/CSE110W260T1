package grouphub.travelshare;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by Christopher on 3/11/2016.
 */
@RunWith(AndroidJUnit4.class)
public class InvitationMessageTest {

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

        onView(withText("You have received an invitation to join Dummy Group! Accept?")).inRoot(isDialog()).check(matches(isDisplayed()));


    }
}
