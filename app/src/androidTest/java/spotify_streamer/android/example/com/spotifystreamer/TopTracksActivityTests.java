package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

/**
 * TopTracksActivity class tests
 * Created by Olga on 7/12/2015.
 */
public class TopTracksActivityTests extends ActivityUnitTestCase<TopTracksActivity> {
    private Intent topTracksActivityIntent;
    private TopTracksActivity topTracksActivity;

    public TopTracksActivityTests() {super(TopTracksActivity.class);}

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        topTracksActivityIntent = new Intent(getInstrumentation().getTargetContext(), TopTracksActivity.class);
        startActivity(topTracksActivityIntent, null, null);
        topTracksActivity = getActivity();
    }

    public void test_TopTracksActivityInitialized(){
        assertNotNull(topTracksActivityIntent);
        assertNotNull(topTracksActivity);
    }
}
