package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;

import java.util.ArrayList;
import java.util.List;

public class HubListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String ARG_PARAM1 = "matches";
    private List<String> matches = new ArrayList<>();
    private OnHubFrag2Listener mListener;
    private ListView hubListView;
    private ListAdapter myListAdapter;

    public HubListFragment() {
    }

    public static HubListFragment newInstance(ArrayList<String> matches) {
        HubListFragment fragment = new HubListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, matches);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matches = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_frag_list, container, false);

        myListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, matches);
        hubListView = (ListView) view.findViewById(R.id.hubListView);
        hubListView.setAdapter(myListAdapter);
        hubListView.setOnItemClickListener(this);

        return view;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        String item = String.valueOf(myListAdapter.getItem(position));
        Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHubFrag2Interaction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHubFrag2Listener) {
            mListener = (OnHubFrag2Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHubFrag2Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnHubFrag2Listener {
        void onHubFrag2Interaction(Uri uri);
    }
}
