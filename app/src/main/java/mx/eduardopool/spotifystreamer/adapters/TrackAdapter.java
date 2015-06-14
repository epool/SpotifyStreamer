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
        TrackHolder trackHolder;
        if (convertView != null) {
            trackHolder = (TrackHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.track_row_item, null);
            trackHolder = new TrackHolder(convertView);
            convertView.setTag(trackHolder);
        }

        TrackBean trackBean = getItem(position);

        Picasso.with(context).load(trackBean.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(trackHolder.trackImageView);
        trackHolder.albumNameTextView.setText(trackBean.getAlbumName());
        trackHolder.trackNameTextView.setText(trackBean.getName());

        return convertView;
    }

    static class TrackHolder {
        @InjectView(R.id.track_image_view)
        ImageView trackImageView;
        @InjectView(R.id.album_name_text_view)
        TextView albumNameTextView;
        @InjectView(R.id.track_name_text_view)
        TextView trackNameTextView;

        public TrackHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}