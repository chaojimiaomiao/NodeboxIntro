package com.market.extension.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.market.extension.R;
import com.market.extension.util.AppItem;
import com.market.extension.widget.AppListAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    //private PageViewModel pageViewModel;
    private ListViewModel chosenViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chosenViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        //pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listView = root.findViewById(R.id.app_list);
        AppListAdapter listAdapter = new AppListAdapter(getContext());
        listView.setAdapter(listAdapter);
        chosenViewModel.getApps().observe(this, new Observer<List<AppItem>>() {
            @Override
            public void onChanged(@Nullable List<AppItem> items) {
                listAdapter.setData(items);
                listAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }
}