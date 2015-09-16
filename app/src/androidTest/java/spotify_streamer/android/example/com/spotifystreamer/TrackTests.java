package spotify_streamer.android.example.com.spotifystreamer;

import junit.framework.TestCase;

/**
 * Tracks class tests
 * Created by Olga on 7/12/2015.
 */
public class TrackTests extends TestCase {
    private String trackName, smallThumbnail, largeThumbnail, albumName, previewUrl;
    private Track track;

    protected void setUp(){
        trackName = "Want to want me";
        albumName = "Everything is 4";
        smallThumbnail = "http://hdjfhgzdskfhkdsvhkjfnvjdf.png";
        largeThumbnail = "http://h67rikjhfyeuhfakwfnvkcnd";
        previewUrl = "http://google.com";
        track = new Track(trackName, albumName, largeThumbnail, smallThumbnail, previewUrl);
    }

    public void testConstructor(){
        assertNotNull(track);
        assertNotNull(track.TrackName);
        assertNotNull(track.SmallThumbnail);
        assertNotNull(track.LargeThumbnail);
        assertNotNull(track.AlbumName);
        assertNotNull(track.PreviewUrl);
    }

    public void testConstructorWithNullArguments(){
        track = new Track(null, null, null, null, null);
        assertNotNull(track);
        assertNull(track.TrackName);
        assertNull(track.AlbumName);
        assertNull(track.SmallThumbnail);
        assertNull(track.LargeThumbnail);
        assertNull(track.PreviewUrl);
    }

    public void testTrackNameGetSet(){
        assertEquals(trackName, track.TrackName);
    }

    public void testAlbumNameGetSet(){
        assertEquals(albumName, track.AlbumName);
    }

    public void testSmallThumbnailGetSet(){
        assertEquals(smallThumbnail, track.SmallThumbnail);
    }

    public void testLargeThumbnailGetSet(){
        assertEquals(largeThumbnail, track.LargeThumbnail);
    }

    public void testPreviewUrlGetSet(){
        assertEquals(previewUrl, track.PreviewUrl);
    }

    public void testToStringMethod(){
        String expectedString = trackName + "--" + albumName + "--" + largeThumbnail + "--" + smallThumbnail + "--" + previewUrl;
        assertEquals(expectedString, track.toString());
    }
}
