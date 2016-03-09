package grouphub.travelshare;


import android.app.Fragment;
import android.app.FragmentManager;

public class HistoryViewModel {
    private String text;
    private String imageUrl;
    private TravelGroup oldGroup;
    private FragmentManager fragmentManager;

    public HistoryViewModel(String text, String imageUrl, TravelGroup oldGroup, FragmentManager fragManager) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.oldGroup = oldGroup;
        this.fragmentManager = fragManager;
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

    public TravelGroup getOldGroup() { return oldGroup; }

    public FragmentManager getFragmentManager() { return fragmentManager; }
}
