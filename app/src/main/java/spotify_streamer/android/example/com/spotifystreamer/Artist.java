package spotify_streamer.android.example.com.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for a custom Artist object with the following properties:
 *  - String Name
 *  - String SpotifyId
 *  - String Thumbnail
 * Created by Olga on 6/28/2015.
 */
public class Artist implements Parcelable {
    String Name;
    String SpotifyId;
    String Thumbnail;

    public Artist(String name, String spotifyId, String thumbnail){
        this.Name = name;
        this.SpotifyId = spotifyId;
        this.Thumbnail = thumbnail;
    }

    private Artist(Parcel in){
        Name = in.readString();
        SpotifyId = in.readString();
        Thumbnail = in.readString();
    }

    @Override
    public int describeContents(){ return 0; }

    public String toString() { return Name + "--" + SpotifyId + "--" + Thumbnail; }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(Name);
        parcel.writeString(SpotifyId);
        parcel.writeString(Thumbnail);
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>(){
        @Override
        public Artist createFromParcel(Parcel parcel){
            return new Artist(parcel);
        }

        @Override
        public Artist[] newArray(int i){
            return new Artist[i];
        }
    };
}
