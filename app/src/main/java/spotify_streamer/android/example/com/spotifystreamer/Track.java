package spotify_streamer.android.example.com.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for a Track object with the following properties:
 *  - String TrackName;
 *  - String AlbumName;
 *  - String LargeThumbnail;
 *  - String SmallThumbnail;
 *  - String PreviewUrl
 * Created by Olga on 6/28/2015.
 */
public class Track implements Parcelable {
    String TrackName;
    String AlbumName;
    String LargeThumbnail;
    String SmallThumbnail;
    String PreviewUrl;

    public Track(String trackName, String albumName, String largeThumbnail, String smallThumbnail, String url){
        this.TrackName = trackName;
        this.AlbumName = albumName;
        this.LargeThumbnail = largeThumbnail;
        this.SmallThumbnail = smallThumbnail;
        this.PreviewUrl = url;
    }

    private Track(Parcel in){
        TrackName = in.readString();
        AlbumName = in.readString();
        LargeThumbnail = in.readString();
        SmallThumbnail = in.readString();
        PreviewUrl = in.readString();
    }

    @Override
    public int describeContents(){ return 0; }

    public String toString() { return TrackName + "--" + AlbumName + "--" + LargeThumbnail + "--" + SmallThumbnail + "--" + PreviewUrl; }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(TrackName);
        parcel.writeString(AlbumName);
        parcel.writeString(LargeThumbnail);
        parcel.writeString(SmallThumbnail);
        parcel.writeString(PreviewUrl);
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>(){
        @Override
        public Track createFromParcel(Parcel parcel){
            return new Track(parcel);
        }

        @Override
        public Track[] newArray(int i){
            return new Track[i];
        }
    };
}
