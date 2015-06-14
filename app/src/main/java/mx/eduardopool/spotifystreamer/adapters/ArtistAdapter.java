package mx.eduardopool.spotifystreamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;

/**
 * Adapter for artists.
 * Created by EduardoPool on 6/9/15.
 */
public class ArtistAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ArtistBean> artistBeans = new ArrayList<>();

    public ArtistAdapter(Context context, ArrayList<ArtistBean> artistBeans) {
        this.context = context;
        this.artistBeans = artistBeans;
    }

    public ArrayList<ArtistBean> getArtistBeans() {
        return artistBeans;
    }

    public void setArtistBeans(ArrayList<ArtistBean> artistBeans) {
        this.artistBeans = artistBeans;
    }

    @Override
    public int getCount() {
        return artistBeans.size();
    }

    @Override
    public ArtistBean getItem(int position) {
        return artistBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            rowView = View.inflate(context, R.layout.artist_row_item, null);
            // configure view holder
            ArtistHolder viewHolder = new ArtistHolder(rowView);
            rowView.setTag(viewHolder);
        }

        ArtistBean artist = getItem(position);

        // fill data
        ArtistHolder holder = (ArtistHolder) rowView.getTag();
        Picasso.with(context).load(artist.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.artistImageView);
        holder.artistNameTextView.setText(artist.getName());

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
