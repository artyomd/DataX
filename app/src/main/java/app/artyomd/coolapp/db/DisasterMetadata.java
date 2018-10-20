package app.artyomd.coolapp.db;

import android.graphics.Bitmap;

import java.util.List;

public class DisasterMetadata {
    private long id;
    private Bitmap image;
    private float longitude;
    private float latitude;
    private List<String> tags;

    public DisasterMetadata() {

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
