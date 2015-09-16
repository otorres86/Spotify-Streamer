package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by Olga on 8/18/2015.
 * Single Track Activity that is triggered by a user clicking on one of the tracks
 * from the top ten tracks list; uses SingleTrackFragment
 */
public class SingleTrackActivity extends AppCompatActivity{

    private static final int DEFAULT_POSITION_VALUE = 0;
    private SingleTrackFragment mFragment;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_track_activity);

        // Create the single track fragment and add it to the activity
        // using a fragment transaction.
        Bundle arguments = new Bundle();
        Intent intent = getIntent();
        arguments.putString(SingleTrackFragment.ARTIST_NAME, intent.getStringExtra(SingleTrackFragment.ARTIST_NAME));
        ArrayList<Track> tracks = intent.getParcelableArrayListExtra(SingleTrackFragment.TOP_TEN_TRACKS_LIST);
        arguments.putParcelableArrayList(SingleTrackFragment.TOP_TEN_TRACKS_LIST, tracks);
        arguments.putInt(SingleTrackFragment.TRACK_POSITION, intent.getIntExtra(SingleTrackFragment.TRACK_POSITION, DEFAULT_POSITION_VALUE));

        mFragment = new SingleTrackFragment();
        mFragment.setArguments(arguments);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.single_track_container, mFragment)
                    .commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
