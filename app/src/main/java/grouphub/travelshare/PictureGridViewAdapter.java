package grouphub.travelshare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

// From here : http://www.mkyong.com/android/android-gridview-example/
public class PictureGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> photoURLs;

    public PictureGridViewAdapter(Context context, List<String> photoURLs) {
        this.context = context;
        this.photoURLs = photoURLs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.picture_gridview, null);

        } else {
            gridView = (View) convertView;
        }

        ImageView imageView = (ImageView) gridView
                .findViewById(R.id.grid_item_image);

        Picasso
                .with(this.context)
                .load(photoURLs.get(position))
                .fit()
                .into(imageView);

        return gridView;
    }

    @Override
    public int getCount() {
        return photoURLs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
