package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

/**
 * MainActivity class tests
 * Created by Olga on 7/12/2015.
 */
public class MainActivityTests extends ActivityUnitTestCase<MainActivity>{

    private Intent mainActivityIntent;
    private MainActivity mainActivity;

    public MainActivityTests() {super(MainActivity.class);}

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivityIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(mainActivityIntent, null, null);
        mainActivity = getActivity();
    }

    public void test_MainActivityInitialized(){
        assertNotNull(mainActivityIntent);
        assertNotNull(mainActivity);
    }
}
