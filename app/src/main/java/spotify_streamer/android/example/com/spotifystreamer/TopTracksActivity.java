package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * TopTracksActivity, uses TopTracksFragment
 * Created by Olga on 7/12/2015.
 */
public class TopTracksActivity extends AppCompatActivity implements TopTracksFragment.Callback{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_tracks_activity);

        //Set activity subtitle if action bar and artist name are not null
        ActionBar actionBar = getSupportActionBar();
        String artistName = getIntent().getStringExtra(TopTracksFragment.EXTRA_ARTIST_NAME);

        if(actionBar != null && artistName != null){
            actionBar.setSubtitle(artistName);
        }

        Bundle arguments = new Bundle();
        arguments.putString(TopTracksFragment.EXTRA_ARTIST_NAME, artistName);
        //SpotifyId
        arguments.putString(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT));

        TopTracksFragment fragment = new TopTracksFragment();
        fragment.setArguments(arguments);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_tracks_fragment, fragment)
                    .commit();
        }

        mTwoPane = getIntent().getBooleanExtra(TopTracksFragment.CONFIGURATION, false);
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

    @Override
    public void onTrackSelected(String artistName, ArrayList<Track> tracks, int position){
        Intent intent = new Intent(this, SingleTrackActivity.class)
                .putExtra(SingleTrackFragment.ARTIST_NAME, artistName)
                .putExtra(SingleTrackFragment.TOP_TEN_TRACKS_LIST, tracks)
                .putExtra(SingleTrackFragment.TRACK_POSITION, position)
                .putExtra(SingleTrackFragment.CONFIGURATION, false);
        startActivity(intent);
    }
}
