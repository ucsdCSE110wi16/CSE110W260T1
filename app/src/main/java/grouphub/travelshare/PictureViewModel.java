package grouphub.travelshare;

// This is where the code came from
// https://www.codementor.io/tips/3473732891/how-to-combine-an-imageview-with-some-text-in-listview

public class PictureViewModel {
    private String text;
    private String imageUrl;

    public PictureViewModel(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
