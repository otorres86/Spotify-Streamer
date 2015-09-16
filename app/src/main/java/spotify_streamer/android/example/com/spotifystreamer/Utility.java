package spotify_streamer.android.example.com.spotifystreamer;

/**
 * Created by Olga on 8/21/2015.
 * Utility class to hold commonly used methods
 */
public class Utility {

    public static boolean isNullOrEmpty(String str){
        if(str != null && !str.isEmpty()){
            return false;
        }
        return true;
    }

    public static String getFormattedTime(int timeInMillis){
        int min = 0;
        int sec = 0;
        sec = (timeInMillis / 1000) % 60;
        min = ((timeInMillis - sec) / 1000)/60;
        return String.format("%d:%02d", min, sec);
    }
}
