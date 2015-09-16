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
 * Custom adapter class for displaying fetched artist data in the
 * corresponding view
 * Created by Olga on 6/28/2015.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    private static final String LOG_TAG = ArtistAdapter.class.getSimpleName();

    /* Custom constructor
    *  @param context        The current context.Used to inflate the layout file.
    *  @param artists        A List of Artist objects to display in a list
    * */
    public ArtistAdapter(Activity context, List<Artist>artists){
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Artist artist = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_listview_item, parent, false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.artist_item_icon);
        //If a thumbnail url is empty, then set a default "no image" image
        if(artist.Thumbnail != null && !artist.Thumbnail.equals(" ")){
            Picasso.with(getContext()).load(artist.Thumbnail).into(iconView);
        }
        else{
            iconView.setImageResource(R.drawable.noimage);
        }

        TextView artistNameView = (TextView) convertView.findViewById(R.id.artist_item_name);
        artistNameView.setText(artist.Name);

        return convertView;
    }
}
