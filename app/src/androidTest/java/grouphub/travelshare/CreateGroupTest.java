package grouphub.travelshare;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
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
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Christopher on 3/6/2016.
 * requires an account to be already logged into the app.
 */
@RunWith(AndroidJUnit4.class)
public class CreateGroupTest {

    @Rule
    public ActivityTestRule<Main> activityTestRule =
            new ActivityTestRule<>(Main.class);

    @Test
    public void CreateGroupCheck() {

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

        onView(withId(R.id.button_creategroup)).perform(click());

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.edittext_prompt)).perform(typeText("CreateGroupScenario"));

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.edittext_prompt)).perform(ViewActions.pressImeActionButton());
        Espresso.closeSoftKeyboard();

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.button_views), FirstViewMatcher.firstView())).perform(click());

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.group_name_text)).check(matches(withText("CreateGroupScenario")));

    }

}


