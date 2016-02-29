package grouphub.travelshare;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class PopupDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText mEditText;
    private String title;
    private int layoutId;

    private static final int REQCODE = 0;
    private static final String EDIT_TEXT_BUNDLE_KEY = "new data";

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public PopupDialog() {
        // Empty constructor required for DialogFragment
    }

    public PopupDialog(String title, int layoutId) {
        this.title = title;
        this.layoutId = layoutId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container);
        mEditText = (EditText) view.findViewById(R.id.edittext_prompt);
        getDialog().setTitle(title);

        mEditText.requestFocus();
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            this.dismiss();
            sendResult();
            return true;
        }
        return false;
    }

    private void sendResult() {
        Intent intent = new Intent();
        intent.putExtra(EDIT_TEXT_BUNDLE_KEY, mEditText.getText().toString());

            getTargetFragment().onActivityResult(
                    getTargetRequestCode(), REQCODE, intent);
    }

    public static int getRequestCode(){ return REQCODE; }
    public static String getKey(){ return EDIT_TEXT_BUNDLE_KEY; }
}
