package grouphub.travelshare;

        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;

        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import static android.support.test.espresso.Espresso.onData;
        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.assertion.ViewAssertions.matches;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;
        import static org.hamcrest.Matchers.anything;
/**
 * Created by Christopher on 2/17/2016.
 * Given, we are logged in and on the homepage.
 * Press the button in the top right to open drop down menu
 * Press logout button
 * result: logged out in the login screen
 */
@RunWith(AndroidJUnit4.class)
public class DLogoutTest {

    @Rule
    public ActivityTestRule<Main> activityTestRule =
            new ActivityTestRule<>(Main.class);

    @Test
    public void LogoutTestCheck() {

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
            Thread.sleep(4000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.login_button)).check(matches(withId(R.id.login_button)));

    }

}
