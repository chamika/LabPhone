package lk.ac.mrt.labphone.fragment;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import lk.ac.mrt.labphone.R;
import lk.ac.mrt.labphone.listener.AngleSensorListener;
import lk.ac.mrt.labphone.view.LevelSingleDimen;
import lk.ac.mrt.labphone.view.LevelTwoDimen;

/**
 * A simple {@link Fragment} subclass.
 */
public class LevelFragment extends Fragment {

    private LevelSingleDimen verticalLevel;
    private LevelSingleDimen horizontalLevel;
    private LevelTwoDimen twoLevel;

    private AngleSensorListener angleSensorListener;

    public LevelFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        verticalLevel = (LevelSingleDimen) view.findViewById(R.id.level_vertical);
        horizontalLevel = (LevelSingleDimen) view.findViewById(R.id.level_horizontal);
        twoLevel = (LevelTwoDimen) view.findViewById(R.id.level_view);

        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        angleSensorListener = new AngleSensorListener(sensorManager, new AngleSensorListener.OnAngleValueChangedListener() {
            @Override
            public void onValueChanged(double x, double y, double z) {
                x = x / Math.PI;
                y = y / Math.PI;
                twoLevel.setLevel(x, y);
                verticalLevel.setLevel(y);
                horizontalLevel.setLevel(x);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        angleSensorListener.registerSensorListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        angleSensorListener.unregisterSensorListener();
    }

}
