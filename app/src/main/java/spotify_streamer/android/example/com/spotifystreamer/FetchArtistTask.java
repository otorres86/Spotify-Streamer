package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/** FetchArtistTask class that fetches Artist data by calling searchArtists() Spotify method
 * with the artist name provided as the first argument to the doInBackground method.
 * If no artists were found, a message is displayed to the user stating that.
 * Created by Olga on 6/28/2015.
 */
public class FetchArtistTask extends AsyncTask<String, Void, Artist[]> {

    private final String LOG_TAG = FetchArtistTask.class.getSimpleName();
    private ArrayAdapter artistAdapter;
    private final Context taskContext;
    private Toast noArtistFoundToast;

    /* Custom Constructor
    *  context - activity that initiated the task
    *  adapter - adapter to use to get the data into views
    */
    public FetchArtistTask(Context context, ArrayAdapter<Artist> adapter){
        taskContext = context;
        artistAdapter = adapter;
    }

    @Override
    protected Artist[] doInBackground(String...params){
        ArtistsPager results = null;
        //No artist name - nothing to look up
        if(params.length == 0){
            return null;
        }
        //Start the spotify service and get the artist by specified name
        try {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            results = spotify.searchArtists(params[0]);
        }
        catch(Exception e){
            Log.e(LOG_TAG, "Error", e);
        }

        return getArtistData(results);
    }

    @Override
    protected void onPostExecute(Artist[] artists){
        if(artistAdapter != null){
            artistAdapter.clear();
            if(artists != null){
                for(Artist artist: artists){
                    artistAdapter.add(artist);
                }
            }
            //If no artists were found, display a toast saying so
            else{
                if(noArtistFoundToast != null){
                    noArtistFoundToast.cancel();
                }
                noArtistFoundToast = Toast.makeText(taskContext, taskContext.getString(R.string.artist_not_found_message), Toast.LENGTH_LONG);
                noArtistFoundToast.setGravity(Gravity.CENTER, 0, 0);
                noArtistFoundToast.show();
            }
        }
    }

    /*Creates an array of Artist objects if data was returned from the searchArtists() query,
    * otherwise, returns null
    */
    private Artist[] getArtistData(ArtistsPager results){
        Artist[] artists = null;

        if(results != null && results.artists != null && results.artists.items != null && results.artists.items.size() != 0){
            List<kaaes.spotify.webapi.android.models.Artist> artistsResults = results.artists.items;
            Integer resultsSize = artistsResults.size();
            artists = new Artist[resultsSize];
            final Integer smallestImageSize = 200;
            for(int i = 0; i < resultsSize; i++) {
                kaaes.spotify.webapi.android.models.Artist artist = artistsResults.get(i);
                String imageUrl = " ";
                //Find the smallest image that is greater than 200
                if (artist.images.size() != 0) {
                    Image smallest = artist.images.get(0);
                    for (int j = 1; j < artist.images.size(); j++) {
                        Image currentImage = artist.images.get(j);
                        Integer currentImageSize = currentImage.height * currentImage.width;
                        if (currentImageSize < (smallest.height * smallest.width) &&
                                currentImageSize >= smallestImageSize) {
                            smallest = currentImage;
                        }
                    }
                    imageUrl = smallest.url;
                }
                if (!Utility.isNullOrEmpty(artist.name) && !Utility.isNullOrEmpty(artist.id)){
                    artists[i] = new Artist(artist.name, artist.id, imageUrl);
                }
            }
        }
        return artists;
    }
}
