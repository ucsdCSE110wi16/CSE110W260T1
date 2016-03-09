package grouphub.travelshare;

// This is where the code came from
// https://www.codementor.io/tips/3473732891/how-to-combine-an-imageview-with-some-text-in-listview

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryListViewAdapter extends BaseAdapter{
    private final List<HistoryViewModel> viewModels;

    private final Context context;
    private final LayoutInflater inflater;

    public HistoryListViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.viewModels = new ArrayList<HistoryViewModel>();
    }

    public HistoryListViewAdapter(Context context, List<HistoryViewModel> viewModels) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.viewModels = viewModels;
    }

    public List<HistoryViewModel> viewmodels() {
        return this.viewModels;
    }

    @Override
    public int getCount() {
        return this.viewModels.size();
    }

    @Override
    public HistoryViewModel getItem(int position) {
        return this.viewModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        // We only need to implement this if we have multiple rows with a different layout. All your rows use the same layout so we can just return 0.
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // We get the view model for this position
        final HistoryViewModel viewModel = getItem(position);

        HistoryRow row;
        // If the convertView is null we need to create it
        if(convertView == null) {
            convertView = this.inflater.inflate(HistoryRow.LAYOUT, parent, false);

            // In that case we also need to create a new row and attach it to the newly created View
            row = new HistoryRow(this.context, convertView);
            convertView.setTag(row);
        }

        // After that we get the row associated with this View and bind the view model to it
        row = (HistoryRow) convertView.getTag();
        row.bind(viewModel);

        return convertView;
    }
}
