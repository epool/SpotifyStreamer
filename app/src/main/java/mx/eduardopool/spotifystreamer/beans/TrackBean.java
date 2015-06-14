package mx.eduardopool.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Track Parcelable.
 * Created by EduardoPool on 6/13/15.
 */
public class TrackBean implements Parcelable {
    public static final Parcelable.Creator<TrackBean> CREATOR = new Parcelable.Creator<TrackBean>() {
        public TrackBean createFromParcel(Parcel source) {
            return new TrackBean(source);
        }

        public TrackBean[] newArray(int size) {
            return new TrackBean[size];
        }
    };
    private String name;
    private String albumName;
    private String imageUrl;
    private String previewUrl;

    public TrackBean() {
    }

    public TrackBean(Track track) {
        this.name = track.name;
        this.albumName = track.album.name;
        // This to get the smallest image size.
        this.imageUrl = track.album.images.isEmpty() ? null : track.album.images.get(track.album.images.size() - 1).url;
        this.previewUrl = track.preview_url;

    }

    protected TrackBean(Parcel in) {
        this.name = in.readString();
        this.albumName = in.readString();
        this.imageUrl = in.readString();
        this.previewUrl = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.albumName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.previewUrl);
    }
}
