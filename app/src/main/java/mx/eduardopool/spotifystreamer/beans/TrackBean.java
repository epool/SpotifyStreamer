package mx.eduardopool.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Image;
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
    private String smallImageUrl;
    private String largeImageUrl;
    private String previewUrl;

    public TrackBean() {
    }

    public TrackBean(Track track) {
        this.name = track.name;
        this.albumName = track.album.name;
        // This to get the smallest image size.
        this.smallImageUrl = track.album.images.isEmpty() ? null : track.album.images.get(track.album.images.size() - 1).url;
        for (Image image : track.album.images) {
            if (image.width == 300) {
                this.largeImageUrl = image.url;
            }
        }
        this.previewUrl = track.preview_url;

    }

    protected TrackBean(Parcel in) {
        this.name = in.readString();
        this.albumName = in.readString();
        this.smallImageUrl = in.readString();
        this.largeImageUrl = in.readString();
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

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackBean)) return false;

        TrackBean trackBean = (TrackBean) o;

        return !(name != null ? !name.equals(trackBean.name) : trackBean.name != null) &&
                !(albumName != null ? !albumName.equals(trackBean.albumName) : trackBean.albumName != null) &&
                !(smallImageUrl != null ? !smallImageUrl.equals(trackBean.smallImageUrl) : trackBean.smallImageUrl != null) &&
                !(largeImageUrl != null ? !largeImageUrl.equals(trackBean.largeImageUrl) : trackBean.largeImageUrl != null) &&
                !(previewUrl != null ? !previewUrl.equals(trackBean.previewUrl) : trackBean.previewUrl != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (albumName != null ? albumName.hashCode() : 0);
        result = 31 * result + (smallImageUrl != null ? smallImageUrl.hashCode() : 0);
        result = 31 * result + (largeImageUrl != null ? largeImageUrl.hashCode() : 0);
        result = 31 * result + (previewUrl != null ? previewUrl.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.albumName);
        dest.writeString(this.smallImageUrl);
        dest.writeString(this.largeImageUrl);
        dest.writeString(this.previewUrl);
    }
}
