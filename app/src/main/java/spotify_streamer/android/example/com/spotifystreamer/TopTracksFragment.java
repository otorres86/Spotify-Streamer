package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * TopTracksFragment: triggers FetchTopTracksTask
 * Created by Olga on 7/12/2015.
 */
public class TopTracksFragment extends Fragment {

    protected final String LOG_TAG = TopTracksFragment.class.getSimpleName();
    static final String EXTRA_ARTIST_NAME = "ARTIST_NAME";
    public static final String CONFIGURATION = "CONFIGURATION";

    private TracksAdapter topTracksAdapter;
    private ArrayList<Track> tracks;
    private String spotifyId;
    private String artistName;
    private boolean mTwoPane;

    public TopTracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("tracks")){
            tracks = new ArrayList<Track>();
        }
        else{
            tracks = savedInstanceState.getParcelableArrayList(("tracks"));
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("tracks", tracks);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "In onCreate method");
        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            artistName = arguments.getString(EXTRA_ARTIST_NAME);

            spotifyId = arguments.getString(Intent.EXTRA_TEXT);
            mTwoPane = arguments.getBoolean(CONFIGURATION);

            topTracksAdapter = new TracksAdapter(getActivity(), tracks);

            ListView listView = (ListView) rootView.findViewById(R.id.top_tracks_listview);
            listView.setAdapter(topTracksAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((Callback) getActivity()).onTrackSelected(artistName, tracks, position);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(!Utility.isNullOrEmpty(spotifyId)) {
            FetchTopTracksTask tracksTask = new FetchTopTracksTask(getActivity(), topTracksAdapter);
            tracksTask.execute(spotifyId);
        }
    }
    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * SingleTrackFragmentCallback for when an item has been selected.
         */
        void onTrackSelected(String artistName, ArrayList<Track> tracks, int position);
    }
}
