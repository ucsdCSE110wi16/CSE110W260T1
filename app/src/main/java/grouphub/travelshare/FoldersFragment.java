package grouphub.travelshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment implements Serializable{
    private transient View view;
    private transient ListView historyView;
    private transient List<HistoryViewModel> viewModels; // to keep track of photos on folders page for listview
    private transient SwipeRefreshLayout swipelayout; // for swipe to refresh
    private transient ToolbarFragment toolbarFragment;

    private static final String TAG = "FoldersFragment";

    private OnFragmentInteractionListener mListener;

    public FoldersFragment() {
        // Required empty public constructor
    }

    public static FoldersFragment newInstance() {
        FoldersFragment fragment = new FoldersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_folders, container, false);

        historyView = (ListView) view.findViewById(R.id.listview_history);

        viewModels = new ArrayList<>();

        initializeFolders();

        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_history);
        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reinitializeFolders();
                swipelayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void setToolbarFragment(ToolbarFragment toolbarFragment) {
        this.toolbarFragment = toolbarFragment;
    }

    private void initializeFolders() {
        ArrayList<TravelGroup> grs;

        try {
            grs = TravelGroup.getTravelGroups(ParseUser.getCurrentUser());

            if(grs == null) {
                return;
            } else {
                addFoldersToView(grs);
            }

        } catch (Exception e){
            Log.d(TAG, "No groups listed for the user");
        }
    }

    private void reinitializeFolders() {
        clearPictures();
        initializeFolders();
    }

    private void clearPictures() {
        viewModels.clear();
    }

    private void addFoldersToView(ArrayList<TravelGroup> groups) {
        for (int i = groups.size()-1 ; i >= 0; i--) {
            String coverPhotoUrl = "";
            ArrayList<Photo> photos = (groups.get(i)).getPhotos();
            HistoryViewModel row;

            if(photos.size() != 0)
                coverPhotoUrl = photos.get(photos.size()-1).getPhotoUrl(); // Use latest photo as cover

            // current group is slightly different -- should take to homepage if clicked and also have marker
            if(i == (groups.size()-1)) {
                row = new HistoryViewModel(groups.get(i).getGroupName(), coverPhotoUrl,
                        groups.get(i), getFragmentManager(), toolbarFragment);
                // pass in toolbarfragment to switch to homepage only if current group
            }
            else{
                // for listview
                row = new HistoryViewModel(groups.get(i).getGroupName(), coverPhotoUrl,
                        groups.get(i), getFragmentManager(), null);
            }

            viewModels.add(row);

        }

        HistoryListViewAdapter listAdapter = new HistoryListViewAdapter(getActivity(), viewModels);
        historyView.setAdapter(listAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
