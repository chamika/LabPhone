package lk.ac.mrt.labphone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import lk.ac.mrt.labphone.MainActivity;
import lk.ac.mrt.labphone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    CardView cvVoltage, cvHeight, cvFrequency, cvAngle, cvLevel;
    private MainActivity mainActivity;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        cvVoltage = (CardView) v.findViewById(R.id.cv_voltage);
        cvVoltage.setOnClickListener(this);

        cvHeight = (CardView) v.findViewById(R.id.cv_height);
        cvVoltage.setOnClickListener(this);

        cvFrequency = (CardView) v.findViewById(R.id.cv_frequency);
        cvFrequency.setOnClickListener(this);

        cvAngle = (CardView) v.findViewById(R.id.cv_angle);
        cvAngle.setOnClickListener(this);

        cvLevel = (CardView) v.findViewById(R.id.cv_level);
        cvLevel.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cv_voltage:
                mainActivity.handleMenuSelect(1);
                break;
            case R.id.cv_height:
                mainActivity.handleMenuSelect(2);
                break;
            case R.id.cv_frequency:
                mainActivity.handleMenuSelect(3);
                break;
            case R.id.cv_angle:
                mainActivity.handleMenuSelect(4);
                break;
            case R.id.cv_level:
                mainActivity.handleMenuSelect(5);
                break;
            default:
                mainActivity.handleMenuSelect(0);
                break;
        }
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
