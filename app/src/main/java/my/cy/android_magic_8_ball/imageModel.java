package my.cy.android_magic_8_ball;

import android.graphics.Bitmap;

/**
 * Created by cylim on 25/05/2016.
 */
public class ImageModel extends Object {
    private String imageURL;
    private Bitmap imageData;

    public ImageModel(String imageURL, Bitmap imageData) {
        this.imageURL = imageURL;
        this.imageData = imageData;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImageData() {
        return imageData;
    }

    public void setImageData(Bitmap imageData) {
        this.imageData = imageData;
    }
}
