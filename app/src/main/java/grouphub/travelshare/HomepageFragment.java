package grouphub.travelshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment implements Serializable{
    private transient View view;
    private transient ListView mainView;
    private transient List<PictureViewModel> viewModels; // to keep track of photos on homepage

    private static final String TAG = "HomepageFragment";

    public HomepageFragment() {
        // Required empty public constructor
        view = null;
    }

    public static HomepageFragment newInstance() {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser()) == null)
            Log.d(TAG, "Problem accessing current travel group");

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_homepage, container, false);

            mainView = (ListView) view.findViewById(R.id.listview_pictures);

            viewModels = new ArrayList<PictureViewModel>();

            initializePictures();
        }

        return view;
    }

    private void initializePictures() {
        TravelGroup gr;

        try {
            gr = TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser());
            ArrayList<Photo> photos = gr.getPhotos();

            if(photos == null) {
                return;
            } else {
                addPicturesToView(photos);
            }

        } catch (Exception e){
            Log.d(TAG, "No groups listed for the user");
        }

    }

    // for adding many pictures
    private void addPicturesToView(ArrayList<Photo> photos) {
        for (int i = photos.size() - 1; i >= 0; i--) {
            PictureViewModel row = new PictureViewModel(photos.get(i).getCityName() + "\n" + photos.get(i).getDate(),
                                                        photos.get(i).getPhotoUrl());
            viewModels.add(row);
        }

        PictureListViewAdapter adapter = new PictureListViewAdapter(getActivity(), viewModels);
        mainView.setAdapter(adapter);
    }

    // for adding a single picture
    public void addPictureToView(Photo photo) {
        PictureViewModel row = new PictureViewModel(photo.getCityName() + "\n" + photo.getDate(),
                photo.getPhotoUrl());
        viewModels.add(row);

        PictureListViewAdapter adapter = new PictureListViewAdapter(getActivity(), viewModels);
        mainView.setAdapter(adapter);
    }

}
