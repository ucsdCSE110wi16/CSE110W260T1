package grouphub.travelshare;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment implements Serializable{
    private transient View view;
    private transient TextView textViewGroupName;
    private transient ListView mainViewList;
    private transient GridView mainViewGrid;
    private transient List<PictureViewModel> viewModels; // to keep track of photos on homepage for listview
    private transient List<String> photoURLs; // to keep track of photos on homepage for gridview
    private transient SwipeRefreshLayout swipelayout; // for swipe to refresh

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

        // check if the current user has any group invitations, if it does, then receive the invitation
        ParseUser currentUser = ParseUser.getCurrentUser();
        String invitation = (String) currentUser.get("invitationID");
        if (!invitation.equals("0") && !invitation.equals("")){
            receiveInvitation(invitation);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser()) == null)
            Log.d(TAG, "Problem accessing current travel group");

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_homepage, container, false);

            textViewGroupName = (TextView) view.findViewById(R.id.group_name_text);
            resetGroupName(); // on top bar

            mainViewList = (ListView) view.findViewById(R.id.listview_pictures);
            mainViewGrid = (GridView) view.findViewById(R.id.gridview_pictures);

            viewModels = new ArrayList<PictureViewModel>();
            photoURLs = new ArrayList<String>();

            initializePictures();

            mainViewGrid.setVisibility(view.INVISIBLE); // hide the grid view and shows listview by default

            swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    reinitializePictures();
                    swipelayout.setRefreshing(false);
                }
            });

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

            mainViewGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int topRowVerticalPosition =
                            (mainViewGrid == null || mainViewGrid.getChildCount() == 0) ?
                                    0 : mainViewGrid.getChildAt(0).getTop();
                    swipelayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                }
            });
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

    private void clearPictures() {
        viewModels.clear();
        photoURLs.clear();
    }

    public void reinitializePictures() {
        clearPictures();
        initializePictures();
    }

    public void resetGroupName(){
        TravelGroup gr = TravelGroup.getActiveTravelGroup(ParseUser.getCurrentUser());
        if( gr != null )
            textViewGroupName.setText(gr.getGroupName());
        else
            textViewGroupName.setText("No Group");
    }

    // for adding many pictures
    private void addPicturesToView(ArrayList<Photo> photos) {
        for (int i = photos.size() - 1; i >= 0; i--) {
            String url = photos.get(i).getPhotoUrl();

            // for listview
            PictureViewModel row = new PictureViewModel(photos.get(i).getCityName() + "\n" + photos.get(i).getDate(), url);
            viewModels.add(row);

            // for gridview
            photoURLs.add(url);
        }

        PictureListViewAdapter listAdapter = new PictureListViewAdapter(getActivity(), viewModels);
        mainViewList.setAdapter(listAdapter);

        PictureGridViewAdapter gridAdapter = new PictureGridViewAdapter(getActivity(), photoURLs);
        mainViewGrid.setAdapter(gridAdapter);
    }

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

    protected void receiveInvitation(String groupID){

        // get the group object based off the groupID
        final TravelGroup group = TravelGroup.getTravelGroup(groupID);
        String groupName = (String) group.get("groupName");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked, accept the invitation!
                        acceptInvitation(group);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked, reject the invitation
                        rejectInvitation();
                        break;
                }
            }
        };

        // pop up a dialog box asking the user if they want to accept/reject the invitation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You have received an invitation to join " + groupName + "! Accept?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    protected void acceptInvitation(TravelGroup group){
        // add the user to the TravelGroup
        group.addUser(ParseUser.getCurrentUser());

        // set the invitations the user has to none
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("invitationID", "0");

        //UPDATE HOMEPAGE GROUPNAME TITLE AND PICTURES IF USER ACCEPTED INVITE TO A NEW GROUP
        reinitializePictures();
        resetGroupName();
    }

    protected void rejectInvitation(){
        // set the invitations the user has to none
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("invitationID", "0");
    }

}
