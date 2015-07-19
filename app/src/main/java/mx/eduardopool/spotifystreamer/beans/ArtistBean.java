package mx.eduardopool.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Artist Parcelable
 * Created by EduardoPool on 6/13/15.
 */
public class ArtistBean implements Parcelable {
    public static final Parcelable.Creator<ArtistBean> CREATOR = new Parcelable.Creator<ArtistBean>() {
        public ArtistBean createFromParcel(Parcel source) {
            return new ArtistBean(source);
        }

        public ArtistBean[] newArray(int size) {
            return new ArtistBean[size];
        }
    };
    private String id;
    private String name;
    private String imageUrl;

    public ArtistBean() {
    }

    public ArtistBean(Artist artist) {
        this.id = artist.id;
        this.name = artist.name;
        // This to get the smallest image size.
        this.imageUrl = artist.images.isEmpty() ? null : artist.images.get(artist.images.size() - 1).url;
    }

    protected ArtistBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtistBean)) return false;

        ArtistBean that = (ArtistBean) o;

        return !(id != null ? !id.equals(that.id) : that.id != null) &&
                !(name != null ? !name.equals(that.name) : that.name != null) &&
                !(imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
    }
}
