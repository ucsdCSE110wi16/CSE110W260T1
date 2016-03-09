package grouphub.travelshare;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Gridview has not been implemented here
public class OldGroupFragment extends Fragment implements Serializable {
    private transient View view;
    private transient TextView textViewGroupName;
    private transient ListView mainViewList;
    private transient GridView mainViewGrid;
    private transient List<PictureViewModel> viewModels; // to keep track of photos on homepage for listview
//    private transient List<String> photoURLs; // to keep track of photos on homepage for gridview
    private transient SwipeRefreshLayout swipelayout; // for swipe to refresh //TODO: not needed
    private transient TravelGroup oldTravelGroup;

    private static final String TAG = "HomepageFragment";
    private static final String OLD_GROUP_KEY = "old_group_key";

    public OldGroupFragment() {
        // Required empty public constructor
        view = null;
    }

    public static OldGroupFragment newInstance(TravelGroup oldGroup) {
        OldGroupFragment fragment = new OldGroupFragment();
        Bundle args = new Bundle();
        args.putSerializable(OLD_GROUP_KEY, oldGroup);
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

            oldTravelGroup = (TravelGroup) getArguments().getSerializable(
                    OLD_GROUP_KEY);

            textViewGroupName = (TextView) view.findViewById(R.id.group_name_text);
            resetGroupName(); // on top bar

            mainViewList = (ListView) view.findViewById(R.id.listview_pictures);
//            mainViewGrid = (GridView) view.findViewById(R.id.gridview_pictures);

            viewModels = new ArrayList<PictureViewModel>();
//            photoURLs = new ArrayList<String>();

            initializePictures();

//            mainViewGrid.setVisibility(view.INVISIBLE); // hide the grid view and shows listview by default

            swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

            // next bit of code with setonscrolllistener is a bit of a workaround for having multiple
            // list/grid views inside a swiperefreshlayout
            // allows scrolling up without forced refreshing
            mainViewList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int topRowVerticalPosition =
                            (mainViewList == null || mainViewList.getChildCount() == 0) ?
                                    0 : mainViewList.getChildAt(0).getTop();
                    swipelayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                }
            });

//            mainViewGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//                }
//
//                @Override
//                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                    int topRowVerticalPosition =
//                            (mainViewGrid == null || mainViewGrid.getChildCount() == 0) ?
//                                    0 : mainViewGrid.getChildAt(0).getTop();
//                    swipelayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
//                }
//            });
        }

        return view;
    }

    private void initializePictures() {
        TravelGroup gr;

        try {
            ArrayList<Photo> photos = oldTravelGroup.getPhotos();

            if(photos == null) {
                return;
            } else {
                addPicturesToView(photos);
            }

        } catch (Exception e){
            Log.d(TAG, "No groups listed for the user");
        }

    }

    private void clearPictures() {
        viewModels.clear();
//        photoURLs.clear();
    }

    public void reinitializePictures() {
        clearPictures();
        initializePictures();
    }

    public void resetGroupName() {
        textViewGroupName.setText(oldTravelGroup.getGroupName());
    }

    // for adding many pictures
    private void addPicturesToView(ArrayList<Photo> photos) {
        for (int i = photos.size() - 1; i >= 0; i--) {
            String url = photos.get(i).getPhotoUrl();

            // for listview
            PictureViewModel row = new PictureViewModel(photos.get(i).getCityName() + "\n" + photos.get(i).getDate(), url);
            viewModels.add(row);

//            // for gridview
//            photoURLs.add(url);
        }

        PictureListViewAdapter listAdapter = new PictureListViewAdapter(getActivity(), viewModels);
        mainViewList.setAdapter(listAdapter);

//        PictureGridViewAdapter gridAdapter = new PictureGridViewAdapter(getActivity(), photoURLs);
//        mainViewGrid.setAdapter(gridAdapter);
    }

    // TODO: No switch view currently, only listview
    // To switch between gridview and listview
    public void switchView() {
        if(mainViewGrid.getVisibility() == view.GONE) {
            mainViewGrid.setVisibility(view.VISIBLE);
            mainViewList.setVisibility(view.GONE);
        } else {
            mainViewGrid.setVisibility(view.GONE);
            mainViewList.setVisibility(view.VISIBLE);
        }
    }
}

