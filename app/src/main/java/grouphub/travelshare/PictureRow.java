package grouphub.travelshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

// This is where the code came from
// https://www.codementor.io/tips/3473732891/how-to-combine-an-imageview-with-some-text-in-listview

public class PictureRow {

    public static final int LAYOUT = R.layout.picture_listview;
    private static final String TAG = "PictureRow";
    private static final long longClickDuration = 2000;

    private final Context context;
    private final TextView textView;
    private final ImageView imageView;
    private long current = 0;
    private String text = "";

    public PictureRow(Context context, View convertView) {
        this.context = context;
        this.imageView = (ImageView) convertView.findViewById(R.id.picture);
        this.textView = (TextView) convertView.findViewById(R.id.where_when);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    current = (long) System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - current) > longClickDuration) {
                        /* Long Click */
                        // Create popup dialog click listener
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked, save image to the phone!
                                        saveImagetoPhone();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked,
                                        break;
                                }
                            }
                        };

                        // pop up a dialog box asking the user if they want to save the image
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Do you want to save the photo to your gallery?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                        return false;
                    } else {
                        // Short Click
                        return true;
                    }
                }
                return true;
            }
        });
    }

    private void saveImagetoPhone() {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", this.textView.getText().toString());
    }

    public void bind(PictureViewModel viewModel) {
        this.textView.setText(viewModel.getText());
        Picasso
            .with(this.context)
                .load(viewModel.getImageUrl())
                .fit()
                .into(this.imageView);
    }
}