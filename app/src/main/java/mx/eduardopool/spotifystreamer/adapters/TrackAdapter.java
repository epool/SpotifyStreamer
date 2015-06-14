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
import mx.eduardopool.spotifystreamer.beans.TrackBean;

/**
 * Adapter for tracks.
 * Created by EduardoPool on 6/9/15.
 */
public class TrackAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TrackBean> trackBeans;

    public TrackAdapter(Context context, ArrayList<TrackBean> trackBeans) {
        this.context = context;
        this.trackBeans = trackBeans;
    }

    public ArrayList<TrackBean> getTrackBeans() {
        return trackBeans;
    }

    public void setTrackBeans(ArrayList<TrackBean> trackBeans) {
        this.trackBeans = trackBeans;
    }

    @Override
    public int getCount() {
        return trackBeans.size();
    }

    @Override
    public TrackBean getItem(int position) {
        return trackBeans.get(position);
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
            rowView = View.inflate(context, R.layout.track_row_item, null);
            // configure view holder
            TrackHolder viewHolder = new TrackHolder(rowView);
            rowView.setTag(viewHolder);
        }

        TrackBean trackBean = getItem(position);

        // fill data
        TrackHolder holder = (TrackHolder) rowView.getTag();
        Picasso.with(context).load(trackBean.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.trackImageView);
        holder.albumNameTextView.setText(trackBean.getAlbumName());
        holder.trackNameTextView.setText(trackBean.getName());

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