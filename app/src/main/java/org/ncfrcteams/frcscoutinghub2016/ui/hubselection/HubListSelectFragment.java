package org.ncfrcteams.frcscoutinghub2016.ui.hubselection;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.network.query.HostDetails;

import java.util.ArrayList;
import java.util.List;

public class HubListSelectFragment extends Fragment {
    private static final String ARG_PARAM = "ARG_PARAM";

    private List<HostDetails> hostDetailsList;
    private ListView listView;

    private OnHostSelectListener listener;

    public HubListSelectFragment() {}

    public static HubListSelectFragment newInstance(ArrayList<String> hostNameArray) {
        HubListSelectFragment fragment = new HubListSelectFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM, hostNameArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] hostNameArray = (String[]) savedInstanceState.getStringArrayList(ARG_PARAM).toArray();
        listView = (ListView) getView().findViewById(R.id.listView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, hostNameArray);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                listener.onHostSelect(hostDetailsList.get(position));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hub_list_select, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHostSelectListener) {
            listener = (OnHostSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHostSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnHostSelectListener {
        void onHostSelect(HostDetails hostDetails);
    }
}
