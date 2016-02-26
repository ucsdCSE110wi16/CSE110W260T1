package grouphub.travelshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureFragment extends Fragment {
    View view;

    private String comment;
    private byte[] picData;
    private String where;
    private String when;

    public PictureFragment() {
        // Required empty public constructor
    }

    public static PictureFragment newInstance(String paramComment, byte[] picData, String paramWhere, String paramWhen) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putString("Comment", paramComment);
        args.putByteArray("PictureId", picData);
        args.putString("Where", paramWhere);
        args.putString("When", paramWhen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comment = getArguments().getString("Comment", "");
        picData = getArguments().getByteArray("PictureId");
        where = getArguments().getString("Where", "");
        when = getArguments().getString("When", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_picture, container, false);

        ImageView iv = (ImageView) view.findViewById(R.id.picture);
        Bitmap bmp = BitmapFactory.decodeByteArray(picData, 0, picData.length);

/*
        // http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), bmp, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
*/




        iv.setImageBitmap(bmp);
        TextView tv_comment = (TextView) view.findViewById(R.id.comment);
        tv_comment.setText(comment);
        TextView tv_where_when = (TextView) view.findViewById(R.id.where_when);
        tv_where_when.setText(where+" / "+when);

        return view;
    }

}
