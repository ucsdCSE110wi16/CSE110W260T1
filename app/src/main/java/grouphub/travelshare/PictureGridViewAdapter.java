package grouphub.travelshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// From here : http://www.mkyong.com/android/android-gridview-example/
public class PictureGridViewAdapter extends BaseAdapter {

    private static final String TAG = "PictureGridViewAdapter";
    private static final long longClickDuration = 2000;

    private Context context;
    private List<String> photoURLs;
    private long current;

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

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    current = (long) System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - current) > longClickDuration) {
                        /* Long Click*/
                        // Create popup dialog click listener
                        final View view = v;
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked, save image to the phone!
                                        saveImagetoPhone(view);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked,
                                        break;
                                }
                            }
                        };

                        // pop up a dialog box asking the user if they want to accept/reject the invitation
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

        return gridView;
    }

    private void saveImagetoPhone(View v) {
        ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        String url = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, UUID.randomUUID().toString() + ".jpeg", "");
        if(url == null) {
            File sdcard = Environment.getExternalStorageDirectory();
            if (sdcard != null) {
                File mediaDir = new File(sdcard, "/sdcard/Camera");
                if (!mediaDir.exists()) {
                    mediaDir.mkdirs();
                }
                url = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, UUID.randomUUID().toString() + ".jpeg", "");
                if(url == null) {
                    Toast.makeText(context, "Failed to save image to gallery!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            Toast.makeText(context, "Saved image to gallery successfully!", Toast.LENGTH_LONG).show();
        }
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
