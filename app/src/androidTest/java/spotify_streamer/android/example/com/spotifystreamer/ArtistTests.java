package spotify_streamer.android.example.com.spotifystreamer;

import junit.framework.TestCase;

/**
 * Artist test class
 * Created by Olga on 7/12/2015.
 */
public class ArtistTests extends TestCase{
    private String name;
    private String id;
    private String thumbnail;
    private Artist artist;

    protected void setUp(){
        name = "Coldplay";
        id = "hfgjdht4758yt78eyrghueiry";
        thumbnail = "http://hdjfhgzdskfhkdsvhkjfnvjdf.png";
        artist = new Artist(name, id, thumbnail);
    }

    public void testConstructor(){
        assertNotNull(artist);
        assertNotNull(artist.Name);
        assertNotNull(artist.SpotifyId);
        assertNotNull(artist.Thumbnail);
    }

    public void testConstructorWithNullArguments(){
        artist = new Artist(null, null, null);
        assertNotNull(artist);
        assertNull(artist.Name);
        assertNull(artist.SpotifyId);
        assertNull(artist.Thumbnail);
    }

    public void testNameGetSet(){
        assertEquals(name, artist.Name);
    }

    public void testSpotifyIdGetSet(){
        assertEquals(id, artist.SpotifyId);
    }

    public void testThumbnailGetSet(){
        assertEquals(thumbnail, artist.Thumbnail);
    }

    public void testToStringMethod(){
        String expectedString = name + "--" + id + "--" + thumbnail;
        assertEquals(expectedString, artist.toString());
    }

}
