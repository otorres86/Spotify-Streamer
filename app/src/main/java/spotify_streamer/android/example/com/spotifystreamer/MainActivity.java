package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/*
*  MainActivity, uses MainActivityFragment class
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback, TopTracksFragment.Callback{

    private boolean mTwoPane;
    private static final String TOPTRACKSFRAGMENT_TAG = "TTTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.top_tracks_fragment) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_tracks_fragment, new TopTracksFragment(), TOPTRACKSFRAGMENT_TAG)
                        .commit();
            }
        }
        else{
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public void onItemSelected(Artist artist){
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putString(MainActivityFragment.EXTRA_ARTIST_NAME, artist.Name);
            args.putString(Intent.EXTRA_TEXT, artist.SpotifyId);
            args.putBoolean(TopTracksFragment.CONFIGURATION, mTwoPane);
            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_fragment, fragment, TOPTRACKSFRAGMENT_TAG)
                    .commit();
        }
        else{
            Intent topTracksIntent = new Intent(this, TopTracksActivity.class).putExtra(Intent.EXTRA_TEXT, artist.SpotifyId)
                    .putExtra(TopTracksFragment.CONFIGURATION, mTwoPane);
            //Store artist name to display in the action bar in TopTracks activity
            topTracksIntent.putExtra(MainActivityFragment.EXTRA_ARTIST_NAME, artist.Name);
            startActivity(topTracksIntent);
        }
    }

    @Override
    public void onTrackSelected(String artistName, ArrayList<Track> tracks, int position){
        Bundle args = new Bundle();
        args.putString(SingleTrackFragment.ARTIST_NAME, artistName);
        args.putParcelableArrayList(SingleTrackFragment.TOP_TEN_TRACKS_LIST, tracks);
        args.putInt(SingleTrackFragment.TRACK_POSITION, position);
        args.putBoolean(SingleTrackFragment.CONFIGURATION, true);
        SingleTrackFragment singleTrackFragment = new SingleTrackFragment();
        singleTrackFragment.setArguments(args);
        singleTrackFragment.show(getSupportFragmentManager(), "dialog");
    }
}
