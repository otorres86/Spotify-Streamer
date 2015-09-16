package spotify_streamer.android.example.com.spotifystreamer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;

import spotify_streamer.android.example.com.spotifystreamer.SingleTrackFragment;

/**
 * Created by Olga on 9/8/2015.
 * MediaPlayerService class that implements onPreparedListener, onErrorListener, and onCompletionListener
 * The class is responsible for initializing, starting, pausing, and seeking the media player
 */
public class MediaPlayerService
    extends Service implements MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
        public static final String ACTION_PLAY = "PLAY";
        public static final String ACTION_PAUSE = "PAUSE";
        public static final String ACTION_SEEK = "SEEK";

        private final IBinder mBinder = new MediaPlayerBinder();
        private MediaPlayer mMediaPlayer;
        WifiManager.WifiLock wifiLock = null;
        private String mPreviewUrl;
        private int mTrackCurrentPosition;

        private ResultReceiver playerReceiver;

        public MediaPlayerService(){
            super();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mp.reset();
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            if(mTrackCurrentPosition > 0) mp.seekTo(mTrackCurrentPosition);
            playerReceiver.send(1, null);
        }

        @Override
        public void onDestroy(){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            wifiLock.release();
        }

        @Override
        public IBinder onBind(Intent intent) {
            if(intent != null){
                playerReceiver = intent.getParcelableExtra(SingleTrackFragment.RESULT_RECEIVER);
            }
            return mBinder;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            playerReceiver.send(2, null);
        }

        //Class for accessing the service from outside the service class
        public class MediaPlayerBinder extends Binder {
            public MediaPlayerService getService(){
                return MediaPlayerService.this;
            }
        }

        //Method that gets called each time a user clicks on media player controls or track
        //has stopped playing to trigger the playback of the current url
        public void playTrack(String action, String previewUrl, int position){
            if(mMediaPlayer == null) {
                initializeMediaPlayer();
                wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                        .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
                wifiLock.acquire();
            }
            switch (action) {
                case ACTION_PLAY:
                    mTrackCurrentPosition = position;
                    if(!previewUrl.equals(mPreviewUrl)){
                        mMediaPlayer.reset();
                        setPreviewTrack(previewUrl);
                    }
                    else{
                        mMediaPlayer.start();
                        playerReceiver.send(1, null);
                    }
                    break;
                case ACTION_PAUSE:
                    if(mMediaPlayer.isPlaying()){
                        mMediaPlayer.pause();
                    }
                    break;
                case ACTION_SEEK:
                    mMediaPlayer.seekTo(position);
                    break;
            }
        }

        //Gets the duration of the current track
        public int getTrackDuration(){
            if(mMediaPlayer != null){
                return mMediaPlayer.getDuration();
            }
            return 0;
        }

        //Gets the current position in the track
        public int getTrackProgress(){
            if(mMediaPlayer != null){
                return mMediaPlayer.getCurrentPosition();
            }
            return 0;
        }

        //Returns a boolean to indicate whether the media player is playing
        public boolean isPlaying() {
            if(mMediaPlayer != null){
                return mMediaPlayer.isPlaying();
            }
            return false;
        }

        //Helper method to initialize the media player
        private void initializeMediaPlayer(){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        }

        //Sets the preview track and calls prepareAsync
        private void setPreviewTrack(String previewUrl) {
            try{
                mMediaPlayer.setDataSource(previewUrl);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            mPreviewUrl = previewUrl;
            try {
                mMediaPlayer.prepareAsync();
            } catch(IllegalStateException e){
                e.printStackTrace();
            }
        }
}
