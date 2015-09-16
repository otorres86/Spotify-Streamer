package spotify_streamer.android.example.com.spotifystreamer;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Olga on 8/23/2015.
 * Custom receiver class to be used to pass data from MediaPlayerService
 * to SingleTrackFragment
 * Referenced: http://stackoverflow.com/questions/4510974/using-resultreceiver-in-android
 */
public class TrackResultReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public TrackResultReceiver(Handler handler){
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }
}
