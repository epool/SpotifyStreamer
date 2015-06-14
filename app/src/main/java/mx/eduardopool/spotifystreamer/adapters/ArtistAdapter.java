package mx.eduardopool.spotifystreamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
        ArtistHolder artistHolder;
        if (convertView != null) {
            artistHolder = (ArtistHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.artist_row_item, null);
            artistHolder = new ArtistHolder(convertView);
            convertView.setTag(artistHolder);
        }

        ArtistBean artist = getItem(position);

        Picasso.with(context).load(artist.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(artistHolder.artistImageView);
        artistHolder.artistNameTextView.setText(artist.getName());

        return convertView;
    }

    static class ArtistHolder {
        @InjectView(R.id.artist_image_view)
        ImageView artistImageView;
        @InjectView(R.id.artist_name_text_view)
        TextView artistNameTextView;

        public ArtistHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
