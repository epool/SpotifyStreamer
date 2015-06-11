package mx.eduardopool.spotifystreamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import mx.eduardopool.spotifystreamer.R;

/**
 * Adapter for artists.
 * Created by EduardoPool on 6/9/15.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    public ArtistAdapter(Context context, List<Artist> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            rowView = View.inflate(getContext(), R.layout.artist_row_item, null);
            // configure view holder
            ArtistHolder viewHolder = new ArtistHolder(rowView);
            rowView.setTag(viewHolder);
        }

        Artist artist = getItem(position);

        String artistImageUrl = artist.images.isEmpty() ? null : artist.images.get(artist.images.size() - 1).url;

        // fill data
        ArtistHolder holder = (ArtistHolder) rowView.getTag();
        Picasso.with(getContext()).load(artistImageUrl).placeholder(R.mipmap.ic_launcher).into(holder.artistImageView);
        holder.artistNameTextView.setText(artist.name);

        return rowView;
    }

    private static class ArtistHolder {
        private ImageView artistImageView;
        private TextView artistNameTextView;

        public ArtistHolder(View view) {
            artistImageView = (ImageView) view.findViewById(R.id.artist_image_view);
            artistNameTextView = (TextView) view.findViewById(R.id.artist_name_text_view);
        }
    }

}
