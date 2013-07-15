package com.muzima.view.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muzima.R;
import com.muzima.adapters.FormsListAdapter;
import com.muzima.listeners.EmptyListListener;
import com.muzima.tasks.DownloadFormTask;
import com.muzima.utils.Fonts;

public class FormsListFragment extends Fragment implements EmptyListListener {

    private static final String TAG = "FormsListFragment";
    private ListView formsList;
    private View noDataLayout;
    private TextView noDataMsgTextView;
    private TextView noDataTipTextView;

    private String noDataMsg;
    private String noDataTip;
    private FormsListAdapter listAdapter;

    public static FormsListFragment newInstance(FormsListAdapter listAdapter, String noDataMsg, String noDataTip) {
        FormsListFragment f = new FormsListFragment();
        f.noDataMsg = noDataMsg;
        f.noDataTip = noDataTip;
        f.listAdapter = listAdapter;
        f.setRetainInstance(true);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View formsLayout = inflater.inflate(R.layout.layout_forms, container, false);
        formsList = (ListView) formsLayout.findViewById(R.id.forms_list);

        setupNoDataView(formsLayout);

        // Todo no need to do this check after all list adapters are implemented
        if (listAdapter != null) {
            formsList.setAdapter(listAdapter);
            listAdapter.setEmptyListListener(this);
        } else {
            listIsEmpty(true);
        }

        return formsLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupNoDataView(View formsLayout) {
        noDataLayout = formsLayout.findViewById(R.id.no_data_layout);
        noDataMsgTextView = (TextView) formsLayout.findViewById(R.id.no_data_msg);
        noDataMsgTextView.setText(noDataMsg);
        noDataTipTextView = (TextView) formsLayout.findViewById(R.id.no_data_tip);
        noDataTipTextView.setText(noDataTip);
        noDataMsgTextView.setTypeface(Fonts.roboto_bold_condensed(getActivity()));
        noDataTipTextView.setTypeface(Fonts.roboto_light(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.reloadData();
        }
    }

    public void downloadComplete(Integer[] status) {
        Integer downloadStatus = status[0];
        String msg = "Download Complete";
        if (downloadStatus == DownloadFormTask.SUCCESS) {
            msg = "Forms downloaded: " + status[1];
            listAdapter.reloadData();
        }else if(downloadStatus == DownloadFormTask.ERROR){
            msg = "An error occurred while downloading forms";
        }else if(downloadStatus == DownloadFormTask.CANCELLED){
            msg = "Form download task has been cancelled";
        }
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void listIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            formsList.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            formsList.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }
    }
}
