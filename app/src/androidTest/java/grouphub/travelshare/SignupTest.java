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
public class SignupTest {

    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<Signup> activityTestRule =
            new ActivityTestRule<>(Signup.class);

    //looks for and edittext with id = emailSignUp
    //types the text "EspressoTestSignup"
    //verifies the edittext has text "EspressoTestSignup"

    @Test
    public void SignupTestCheck() {

        onView(withId(R.id.emailSignUp)).perform(typeText("EspressoTestSignup")).check(matches(withText("EspressoTestSignup")));
        onView(withId(R.id.username)).perform(typeText("DeleteBeforeSignupTest")).check(matches(withText("DeleteBeforeSignupTest")));
        onView(withId(R.id.passwordSignUp)).perform(typeText("espresso")).check(matches(withText("espresso")));
        onView(withId(R.id.confpasswordSignUp)).perform(typeText("espresso")).check(matches(withText("espresso")));

        onView(withId(R.id.signupButton)).perform(click());

        try {
            Thread.sleep(8000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.listview_pictures)).check(matches(withId(R.id.listview_pictures)));
    }

}
