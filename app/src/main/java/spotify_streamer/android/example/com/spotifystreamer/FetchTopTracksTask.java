package spotify_streamer.android.example.com.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;

/** FetchTopTracksTask that fethches top ten tracks for the specified artist by
 * calling getArtistTopTrack() method from the Spotify API
 * Created by Olga on 7/12/2015.
 */
public class FetchTopTracksTask extends AsyncTask<String, Void, Track[]> {

    private final String LOG_TAG = FetchTopTracksTask.class.getSimpleName();
    private ArrayAdapter topTracksAdapter;
    private final Context taskContext;
    private Toast noTracksFoundToast;

    /* Custom constructor
    *  context - activity that initiated the task
    *  adapter - adapter to use to get data displayed in the views
     */
    public FetchTopTracksTask(Context context, ArrayAdapter<Track> adapter) {
        taskContext = context;
        topTracksAdapter = adapter;
    }

    @Override
    protected Track[] doInBackground(String... params) {
        kaaes.spotify.webapi.android.models.Tracks results = null;
        //No artist spotifyId - nothing to look up
        if (params.length == 0) {
            return null;
        }
        //Start the spotify service and get the artist's top ten tracks by specified spotifyId
        try {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            //Get user's country and specify it in the call
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());

            results = spotify.getArtistTopTrack(params[0], options);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error", e);
        }
        return getTracksData(results);
    }

    @Override
    protected void onPostExecute(Track[] tracks) {
        if(topTracksAdapter != null) {
            if (tracks != null) {
                topTracksAdapter.clear();
                for (Track track : tracks) {
                    topTracksAdapter.add(track);
                }
            }
            //If no tracks were found, display a toast
            else {
                if (noTracksFoundToast != null) {
                    noTracksFoundToast.cancel();
                }
                noTracksFoundToast = Toast.makeText(taskContext, taskContext.getString(R.string.no_tracks_found_message), Toast.LENGTH_LONG);
                noTracksFoundToast.setGravity(Gravity.CENTER, 0, 0);
                noTracksFoundToast.show();
            }
        }
    }

    /*
    *  If there were tracks returned from the getArtistTopTrack() call,
    *  creates and returns an Array of Track objects, otherwise, returns null
     */
    private Track[] getTracksData(kaaes.spotify.webapi.android.models.Tracks results) {
        Track[] topTracks = null;
        //Desired sizes for thumbnails
        final Integer smallThumbnailSize = 200;
        final Integer largeThumbnailSize = 640;

        if (results != null && results.tracks != null && results.tracks != null && results.tracks.size() != 0) {
            List<kaaes.spotify.webapi.android.models.Track> tracksResults = results.tracks;
            Integer resultsSize = tracksResults.size();
            topTracks = new Track[resultsSize];
            for (int i = 0; i < resultsSize; i++) {
                kaaes.spotify.webapi.android.models.Track track = tracksResults.get(i);
                String smallImageUrl = " ", largeImageUrl = " ";
                //Find small and large thumbnails that satisfy desired sizes
                if (track.album.images.size() != 0) {
                    Image smallThumbnail = track.album.images.get(0);
                    Image largeThumbnail = track.album.images.get(0);
                    for (int j = 1; j < track.album.images.size(); j++) {
                        Image currentImage = track.album.images.get(j);
                        Integer currentImageSize = currentImage.height * currentImage.width;
                        if (currentImageSize < (smallThumbnail.height * smallThumbnail.width) &&
                                currentImageSize >= smallThumbnailSize) {
                            smallThumbnail = currentImage;
                        } else if (currentImageSize < (largeThumbnail.height * largeThumbnail.width) &&
                                currentImageSize >= largeThumbnailSize) {
                            largeThumbnail = currentImage;
                        }
                    }
                    smallImageUrl = smallThumbnail.url;
                    largeImageUrl = largeThumbnail.url;
                }
                topTracks[i] = new Track(track.name, track.album.name, largeImageUrl, smallImageUrl, track.preview_url);
            }
        }
        return topTracks;
    }
}