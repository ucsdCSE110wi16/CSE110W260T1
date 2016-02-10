package grouphub.travelshare;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomepageFragment extends Fragment {
    View view;
    LinearLayout mainView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomepageFragment() {
        // Required empty public constructor
    }

    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);

        mainView = (LinearLayout) view.findViewById(R.id.homepage_main_view);

        initializePictures();

        return view;
    }

    // Currently just using sample pictures, but later needs to access database and use retrieved data to initialize
    private void initializePictures() {
        addPictureToView("Trevor: Hello Mr. Turtle! \nVladmir: Whoa, Dude. Mr. Turtle is my father", R.drawable.travel1, "Maldives Underwater", "June 17, 2014");
        addPictureToView("Trevor: Swimming with the big fish", R.drawable.travel2, "Maldives Underwater", "June 17, 2014");
        addPictureToView("", R.drawable.travel3, "Maldives Underwater", "June 17, 2014");
        addPictureToView("Kramnik: Duuuuuude \nMelissa: First you were all like whoa, and we were like whoa, and you were like whoa...", R.drawable.travel4, "Maldives Underwater", "June 17, 2014");
        addPictureToView("", R.drawable.travel5, "Maldives", "June 17, 2014");
        addPictureToView("", R.drawable.travel6, "Maldives", "June 17, 2014");
        addPictureToView("Trevor: Well... it's time to eat?\n Katie: Yeah, it's time for them to eat us", R.drawable.travel7, "Maldives", "June 17, 2014");
    }

    private void addPictureToView(String comment, int pictureId, String where, String when) {
        PictureFragment newPic = PictureFragment.newInstance(comment, pictureId, where, when);

        getFragmentManager().beginTransaction().add(R.id.homepage_main_view, newPic, "Hello").commit();
    }
}
