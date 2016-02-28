package grouphub.travelshare;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

// This is where the code came from
// https://www.codementor.io/tips/3473732891/how-to-combine-an-imageview-with-some-text-in-listview

public class PictureRow {

    // This is a reference to the layout we defined above
    public static final int LAYOUT = R.layout.fragment_picture;

    private final Context context;
    private final TextView textView;
    private final ImageView imageView;

    public PictureRow(Context context, View convertView) {
        this.context = context;
        this.imageView = (ImageView) convertView.findViewById(R.id.picture);
        this.textView = (TextView) convertView.findViewById(R.id.where_when);
    }

    public void bind(PictureViewModel exampleViewModel) {
        this.textView.setText(exampleViewModel.getText());
        Picasso
            .with(this.context)
                .load(exampleViewModel.getImageUrl())
                .fit()
                .into(this.imageView);
    }
}