package org.ncfrcteams.frcscoutinghub2016.ui.scout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import org.ncfrcteams.frcscoutinghub2016.R;

import java.util.ArrayList;
import java.util.List;


public class ScoutFragMid extends Fragment implements View.OnClickListener {

    private int orientation;
    private int mode;
    private OnFragListener mListener;
    private List<ImageView> barriers = new ArrayList<>();
    public ScoutFragMid() {
    }

    public static ScoutFragMid newInstance() {
        return new ScoutFragMid();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = ScoutMainActivity.myMatchRecord.get("Orientation");
        mode = ScoutMainActivity.myMatchRecord.get("Mode");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_frag_mid, container, false);
        int[] arrangement = ScoutMainActivity.myMatchRecord.getBarriers();

        for(int i = 0; i < 10; i++){
            int id = getResources().getIdentifier("barrier"+i, "id", getActivity().getPackageName());
            ImageView barrier = (ImageView) view.findViewById(id);
            barrier.setOnClickListener(this);
            barriers.add(barrier);
            if(0<i && i<9){
                int icon = getResources().getIdentifier("barrier" + arrangement[i], "drawable",
                        getActivity().getPackageName());
                barrier.setImageResource(icon);
            }
        }

        if(orientation == 2){
            view.findViewById(R.id.middle).setRotation(180);
        }
        updateFragment();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragListener) {
            mListener = (OnFragListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view){
        String id = view.getResources().getResourceName(view.getId());
        id = "Barrier " + id.charAt(id.length()-1);
        ScoutMainActivity.myMatchRecord.setActiveButton(id);
    }

    public void updateFragment(){
        //set text
    }

    public interface OnFragListener {
        void onFrag2Interaction(Uri uri);
    }
}
