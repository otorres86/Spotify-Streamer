package spotify_streamer.android.example.com.spotifystreamer;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import spotify_streamer.android.example.com.spotifystreamer.service.MediaPlayerService;

/**
 * Created by Olga on 9/8/2015.
 */
public class SingleTrackFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Runnable {

    static final String TOP_TEN_TRACKS_LIST = "TOP_TEN_TRACKS";
    static final String TRACK_POSITION = "TRACK_POSITION";
    static final String ARTIST_NAME = "ARTIST_NAME";

    public static final String RESULT_RECEIVER = "RESULT_RECEIVER";
    public static final String CONFIGURATION = "CONFIGURATION";

    private Track mSelectedTrack;
    private String mArtistNameValue;
    private String mPreviewUrlValue;
    private ArrayList<Track> mTracks;
    private int mTrackPosition;
    private int mTrackCurrentPosition;
    private boolean mBound = false;
    private String mAction;


    private TextView mArtistName;
    private TextView mAlbumName;
    private TextView mTrackName;
    private ImageView mTrackThumbnail;
    private SeekBar mTrackSeekBar;
    private TextView mTrackEndTime;
    private TextView mTrackElapsedTime;

    //Media player controls
    private ImageView mPlayButton;
    private ImageView mPauseButton;
    private ImageView mPreviousButton;
    private ImageView mNextButton;

    private Thread mSeekBarThread;

    public MediaPlayerService mMediaPlayerService;
    public MediaPlayerResultReceiver mPlayerReceiver;


    public SingleTrackFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mTrackCurrentPosition = savedInstanceState.getInt("current_position");
            mTrackPosition = savedInstanceState.getInt("selected_track_pos");
            mAction = savedInstanceState.getString("player_action");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mPlayerReceiver = new MediaPlayerResultReceiver(null);
        Intent serviceIntent = new Intent(getActivity(), MediaPlayerService.class);
        serviceIntent.putExtra(RESULT_RECEIVER, mPlayerReceiver);
        getActivity().bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_position", mTrackCurrentPosition);
        outState.putInt("selected_track_pos", mTrackPosition);
        outState.putString("player_action", mAction);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.track_player, container, false);


        //Get the textViews
        mArtistName = (TextView) rootView.findViewById(R.id.track_artist_name);
        mAlbumName = (TextView) rootView.findViewById(R.id.track_album_name);
        mTrackName = (TextView) rootView.findViewById(R.id.track_name);
        mTrackThumbnail = (ImageView) rootView.findViewById(R.id.track_thumbnail);
        mTrackElapsedTime = (TextView) rootView.findViewById(R.id.track_time_elapsed);
        mTrackEndTime = (TextView) rootView.findViewById(R.id.track_duration);

        Bundle arguments = getArguments();

        if (arguments != null) {
            mArtistNameValue = arguments.getString(ARTIST_NAME);
            mTracks = arguments.getParcelableArrayList(SingleTrackFragment.TOP_TEN_TRACKS_LIST);
            mTrackPosition = (savedInstanceState != null) ? savedInstanceState.getInt("selected_track_pos") :
                    arguments.getInt(SingleTrackFragment.TRACK_POSITION);

        }

        if(mTracks != null && mTracks.size() != 0) {
            getSelectedTrackInfo();
        }
        if(Utility.isNullOrEmpty(mArtistNameValue)){
            mArtistNameValue = getString(R.string.unknown_artist);
        }
        mArtistName.setText(mArtistNameValue);

        mTrackSeekBar = (SeekBar)rootView.findViewById(R.id.track_scrub_bar);
        mPlayButton = (ImageView)rootView.findViewById(R.id.play_track_button);
        mPauseButton = (ImageView)rootView.findViewById(R.id.pause_track_button);
        mPreviousButton = (ImageView)rootView.findViewById(R.id.previous_track_button);
        mNextButton = (ImageView)rootView.findViewById(R.id.next_track_button);

        //Set the seek bar
        mTrackSeekBar.setOnSeekBarChangeListener(this);

        //Set onclick listeners for the media player controls
        mPlayButton.setOnClickListener(this);
        mPauseButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);

        //If there is only one track - disable the previous and next buttons
        if(mTracks.size() == 1){
            mNextButton.setEnabled(false);
            mPreviousButton.setEnabled(false);
        }

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onPause(){
        super.onPause();
        // Unbind from the service
        if (mBound) {
            getActivity().unbindService(mConnection);
            resetSeekBar();
            mBound = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            mMediaPlayerService = binder.getService();

            mBound = true;
            mAction = mAction == null ? MediaPlayerService.ACTION_PLAY : mAction;
            mMediaPlayerService.playTrack(mAction, mPreviewUrlValue, mTrackCurrentPosition);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    //Gets current track position and duration and set the appropriate time labels and seek bar;
    //starts the runnable
    private void startSeekBar() {
        int currentPosition = mMediaPlayerService.getTrackProgress();
        int duration = mMediaPlayerService.getTrackDuration();
        mTrackElapsedTime.setText(Utility.getFormattedTime(currentPosition));
        mTrackSeekBar.setProgress(currentPosition);
        mTrackEndTime.setText(Utility.getFormattedTime(duration));
        mTrackSeekBar.setMax(duration);
        mSeekBarThread = new Thread(this);
        mSeekBarThread.start();
    }

    @Override
    public void onClick(View button) {
        resetSeekBar();
        switch(button.getId()){
            case R.id.play_track_button:
                mPlayButton.setVisibility(View.GONE);
                mPauseButton.setVisibility(View.VISIBLE);
                mAction = MediaPlayerService.ACTION_PLAY;
                break;
            case R.id.pause_track_button:
                mPauseButton.setVisibility(View.GONE);
                mPlayButton.setVisibility(View.VISIBLE);
                mAction = MediaPlayerService.ACTION_PAUSE;
                break;
            case R.id.previous_track_button:
                mPlayButton.setVisibility(View.GONE);
                mPauseButton.setVisibility(View.VISIBLE);
                mTrackCurrentPosition = 0;
                if(mTrackPosition != 0) {
                    mTrackPosition -= 1;
                }
                else{
                    mTrackPosition = mTracks.size() - 1;
                }
                getSelectedTrackInfo();
                mAction = MediaPlayerService.ACTION_PLAY;
                break;
            case R.id.next_track_button:
                mPlayButton.setVisibility(View.GONE);
                mPauseButton.setVisibility(View.VISIBLE);
                mTrackCurrentPosition = 0;
                int lastTrackPosition = mTracks.size() - 1;
                if (mTrackPosition != lastTrackPosition) {
                    mTrackPosition += 1;
                } else {
                    mTrackPosition = 0;
                }
                getSelectedTrackInfo();
                mAction = MediaPlayerService.ACTION_PLAY;
                break;
            default:
                break;
        }

        if(mBound){
            mMediaPlayerService.playTrack(mAction, mPreviewUrlValue, mTrackCurrentPosition);
        }
    }

    //Resets the seek bar
    private void resetSeekBar() {
        if(mSeekBarThread != null){
            mSeekBarThread.interrupt();
            mSeekBarThread = null;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && mBound) {
            mMediaPlayerService.playTrack(MediaPlayerService.ACTION_SEEK, mPreviewUrlValue, progress);
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }


    //Gets the currently selected track's info and sets the corresponding
    //views with the values
    private void getSelectedTrackInfo() {
        mSelectedTrack = mTracks.get(mTrackPosition);
        if (mSelectedTrack != null) {
            mAlbumName.setText(mSelectedTrack.AlbumName);
            mTrackName.setText(mSelectedTrack.TrackName);
            //If no thumbnail, display a default "no image" image
            if (!Utility.isNullOrEmpty(mSelectedTrack.LargeThumbnail)) {
                Picasso.with(getActivity()).load(mSelectedTrack.LargeThumbnail).into(mTrackThumbnail);
            } else {
                mTrackThumbnail.setImageResource(R.drawable.noimage);
            }
            mPreviewUrlValue = mSelectedTrack.PreviewUrl;
        }
    }

    //Runs the thread to update the seek bar and elapsed time
    public void run(){
        if(mBound){
            int currentPosition = mMediaPlayerService.getTrackProgress();
            int trackDuration = mMediaPlayerService.getTrackDuration();
            while(currentPosition < trackDuration){
                try{
                    currentPosition = mMediaPlayerService.getTrackProgress();
                    Thread.sleep(100);
                }
                catch(InterruptedException e){
                    return;
                }
                catch (Exception e) {
                    return;
                }
                mTrackSeekBar.setProgress(currentPosition);
                mTrackCurrentPosition = currentPosition;
                setElapsedTime(currentPosition);
            }
        }
    }

    //Set current track elapsed time
    private void setElapsedTime(final int currentPosition) {
        if(getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTrackElapsedTime.setText(Utility.getFormattedTime(currentPosition));
                Log.v(SingleTrackFragment.class.getSimpleName(), "Set elapsed time: " + Integer.toString(currentPosition));
            }
        });
    }

    //Custom ResultReceiver class to receive messages from MediaPlayerService:
    // 1 - MediaPlayer is prepared and can be used
    // 2 - MediaPlayer has completed playing the previous track
    public class MediaPlayerResultReceiver extends ResultReceiver {
        public MediaPlayerResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData){
            //MediaPlayer is prepared
            if(resultCode == 1){
                startSeekBar();
            }
            //MediaPlayer is done playing the track
            else if(resultCode == 2){
                if(mSeekBarThread != null){
                    mSeekBarThread.interrupt();
                    mSeekBarThread = null;
                }
                //Play the next track in the list
                int lastTrackPosition = mTracks.size() - 1;
                if (mTrackPosition != lastTrackPosition) {
                    mTrackPosition += 1;
                } else {
                    mTrackPosition = 0;
                }
                getSelectedTrackInfo();
                if(mBound){
                    mMediaPlayerService.playTrack(MediaPlayerService.ACTION_PLAY, mPreviewUrlValue, 0);
                }
            }
        }
    }
}
