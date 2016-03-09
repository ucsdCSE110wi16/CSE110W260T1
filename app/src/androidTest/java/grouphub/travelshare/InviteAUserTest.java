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
 * Created by Christopher on 3/7/2016.
 * requires an account to be already logged into the app.
 */
@RunWith(AndroidJUnit4.class)
public class InviteAUserTest {

    @Rule
    public ActivityTestRule<Login> activityTestRule =
            new ActivityTestRule<>(Login.class);

    @Test
    public void InviteAUserCheck() {

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.button_manager), FirstViewMatcher.firstView())).perform(click());


        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.button_inviteusertogroup)).perform(click());

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.edittext_prompt)).perform(typeText("EspressoTestSignup"));

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.edittext_prompt)).perform(ViewActions.pressImeActionButton());

        try {
            Thread.sleep(5000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("User invited!")).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));


        onView(allOf(withId(R.id.button_manager), FirstViewMatcher.firstView())).perform(click());

        try {
            Thread.sleep(5000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }




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
