package spotify_streamer.android.example.com.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom adapter class for displaying fetched artist's top ten tracks in the
 * corresponding view
 * Created by Olga on 7/12/2015.
 */
public class TracksAdapter extends ArrayAdapter<Track>  {

    public TracksAdapter(Activity context, List<Track> tracks){
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Track track = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tracks_listview_item, parent, false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.track_item_icon);
        //If no thumbnail, display a default "no image" image
        if(track.SmallThumbnail != " "){
            Picasso.with(getContext()).load(track.SmallThumbnail).into(iconView);
        }
        else{
            iconView.setImageResource(R.drawable.noimage);
        }

        TextView trackNameView = (TextView) convertView.findViewById(R.id.listview_item_track_name);
        trackNameView.setText(track.TrackName);

        TextView albumNameView = (TextView) convertView.findViewById(R.id.listview_item_album_name);
        albumNameView.setText(track.AlbumName);

        return convertView;
    }
}
