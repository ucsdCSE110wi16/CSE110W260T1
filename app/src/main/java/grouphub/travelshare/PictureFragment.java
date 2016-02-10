package grouphub.travelshare;

import android.content.Context;
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
    private int pictureId;
    private String where;
    private String when;

    public PictureFragment() {
        // Required empty public constructor
    }

    public static PictureFragment newInstance(String paramComment, int paramPictureId, String paramWhere, String paramWhen) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putString("Comment", paramComment);
        args.putInt("PictureId", paramPictureId);
        args.putString("Where", paramWhere);
        args.putString("When", paramWhen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comment = getArguments().getString("Comment", "");
        pictureId = getArguments().getInt("PictureId", 0);
        where = getArguments().getString("Where", "");
        when = getArguments().getString("When", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_picture, container, false);

        ImageView iv = (ImageView) view.findViewById(R.id.picture);
        iv.setImageResource(pictureId);
        TextView tv_comment = (TextView) view.findViewById(R.id.comment);
        tv_comment.setText(comment);
        TextView tv_where_when = (TextView) view.findViewById(R.id.where_when);
        tv_where_when.setText(where+" / "+when);

        return view;
    }

}
