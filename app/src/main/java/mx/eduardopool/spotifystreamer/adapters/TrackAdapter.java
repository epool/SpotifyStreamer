package mx.eduardopool.spotifystreamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import mx.eduardopool.spotifystreamer.R;

/**
 * Adapter for tracks.
 * Created by EduardoPool on 6/9/15.
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    public TrackAdapter(Context context, List<Track> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            rowView = View.inflate(getContext(), R.layout.track_row_item, null);
            // configure view holder
            TrackHolder viewHolder = new TrackHolder(rowView);
            rowView.setTag(viewHolder);
        }

        Track track = getItem(position);

        String artistImageUrl = track.album.images.isEmpty() ? null : track.album.images.get(track.album.images.size() - 1).url;

        // fill data
        TrackHolder holder = (TrackHolder) rowView.getTag();
        Picasso.with(getContext()).load(artistImageUrl).placeholder(R.mipmap.ic_launcher).into(holder.trackImageView);
        holder.albumNameTextView.setText(track.album.name);
        holder.trackNameTextView.setText(track.name);

        return rowView;
    }

    private static class TrackHolder {
        private ImageView trackImageView;
        private TextView albumNameTextView;
        private TextView trackNameTextView;

        public TrackHolder(View view) {
            trackImageView = (ImageView) view.findViewById(R.id.track_image_view);
            albumNameTextView = (TextView) view.findViewById(R.id.album_name_text_view);
            trackNameTextView = (TextView) view.findViewById(R.id.track_name_text_view);
        }
    }

}