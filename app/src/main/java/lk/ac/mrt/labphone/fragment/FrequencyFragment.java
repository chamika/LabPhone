package lk.ac.mrt.labphone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.ac.mrt.labphone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrequencyFragment extends Fragment {


    public FrequencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frequency, container, false);
    }

}
