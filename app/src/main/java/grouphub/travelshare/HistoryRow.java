package grouphub.travelshare;

// This is where the code came from
// https://www.codementor.io/tips/3473732891/how-to-combine-an-imageview-with-some-text-in-listview

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

public class HistoryRow {

    public static final int LAYOUT = R.layout.folder_listview;
    private static final String TAG = "PictureRow";

    private final Context context;
    private final TextView textView;
    private final ImageView imageView;
    private final RelativeLayout rowLayout;

    public HistoryRow(Context context, View convertView) {
        this.context = context;
        this.imageView = (ImageView) convertView.findViewById(R.id.picture_history);
        this.textView = (TextView) convertView.findViewById(R.id.group_name_history);
        this.rowLayout = (RelativeLayout) convertView.findViewById(R.id.row_history);
    }

    public void bind(final HistoryViewModel viewModel) {
        this.textView.setText(viewModel.getText());

        // If a photo exists, use as cover photo
        if(!viewModel.getImageUrl().equals("")) {
            Picasso
                    .with(this.context)
                    .load(viewModel.getImageUrl())
                    .fit()
                    .into(this.imageView);
        } else { // otherwise use default
            Picasso
                    .with(context)
                    .load(R.drawable.no_image_available_md)
                    .fit()
                    .into(imageView);
        }

        rowLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager Manager = viewModel.getFragmentManager();
                FragmentTransaction trans = Manager.beginTransaction();
                trans.replace(R.id.placeholder, OldGroupFragment.newInstance(viewModel.getOldGroup()));
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }
}

