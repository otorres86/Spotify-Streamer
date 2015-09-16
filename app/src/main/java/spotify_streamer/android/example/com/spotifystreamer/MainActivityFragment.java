package spotify_streamer.android.example.com.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

/**
 * MainActivityFragment: sets onItemClickListener for handling clicks on artist entries,
 * sets onQueryTextListener for the search view to capture user input of an artist name,
 * triggers FetchArtistTask
 */
public class MainActivityFragment extends Fragment {

    protected final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private ArtistAdapter artistAdapter;
    private ArrayList<Artist> artists;
    public static final String EXTRA_ARTIST_NAME = "ARTIST_NAME";

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Artist artist);
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("artists")){
            artists = new ArrayList<Artist>();
        }
        else{
            artists = savedInstanceState.getParcelableArrayList(("artists"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("artists", artists);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        artistAdapter = new ArtistAdapter(getActivity(),artists);

        ListView listView = (ListView) rootView.findViewById(R.id.artist_listView);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistAdapter.getItem(position);
                if(artist != null){
                    ((Callback) getActivity()).onItemSelected(artist);
                }
            }
        });

        SearchView searchView = (SearchView) rootView.findViewById(R.id.artist_searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateArtistView(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }

    /*
    * Starts a FetchArtistTask to get a list of items for the specified artist name
     */
    private void updateArtistView(String query) {
        FetchArtistTask artistTask = new FetchArtistTask(getActivity(), artistAdapter);
        artistTask.execute(query);
    }
}
